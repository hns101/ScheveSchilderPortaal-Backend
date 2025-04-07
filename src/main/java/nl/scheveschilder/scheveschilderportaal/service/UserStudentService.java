package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.UserStudentDto;
import nl.scheveschilder.scheveschilderportaal.models.Role;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.models.User;
import nl.scheveschilder.scheveschilderportaal.repositories.RoleRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserStudentService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;


    public UserStudentService(
            StudentRepository studentRepo,
            UserRepository userRepo,
            RoleRepository roleRepo,
            PasswordEncoder passwordEncoder  // ✅ Add this
    ) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;  // ✅ Save it
    }

    public StudentDto createUserAndStudent(StudentDto dto) {
        // Don't check studentRepo.existsById(dto.id); we want to generate it

        User user = userRepo.findById(dto.email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(dto.email);
            newUser.setPassword("changeme");
            Role roleUser = roleRepo.findById("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("Rol 'ROLE_USER' niet gevonden."));
            newUser.setRoles(Set.of(roleUser));
            return userRepo.save(newUser);
        });

        if (user.getStudent() != null) {
            throw new IllegalStateException("Deze gebruiker is al gekoppeld aan een andere student.");
        }

        Student student = new Student();
        student.setFirstname(dto.firstname);
        student.setLastname(dto.lastname);
        student.setDefaultSlot(dto.defaultSlot);
        student.setActiveMember(true);
        student.setUser(user);

        studentRepo.save(student);

        return dto;
    }

    public List<UserStudentDto> getAllUsersWithStudents() {
        return userRepo.findAll().stream()
                .map(UserStudentDto::fromEntity)
                .toList();
    }

    public UserStudentDto getUserByEmail(String email) {
        return userRepo.findById(email)
                .map(UserStudentDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker met email '" + email + "' niet gevonden."));
    }

    public void deleteUser(String email) {
        User user = userRepo.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker met email '" + email + "' niet gevonden."));

        if (user.getStudent() != null) {
            Student student = user.getStudent();

            // Remove student from all lessons first
            student.getLessons().forEach(lesson -> lesson.getStudents().remove(student));
            studentRepo.save(student); // update DB to reflect changes

            studentRepo.delete(student); // now safe to delete
        }

        userRepo.delete(user);
    }

    public UserStudentDto updateUser(String email, UserStudentDto input) {
        User user = userRepo.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker met email '" + email + "' niet gevonden."));

        if (input.roles != null && !input.roles.isEmpty()) {
            Set<Role> roles = input.roles.stream()
                    .map(roleName -> roleRepo.findById(roleName)
                            .orElseThrow(() -> new IllegalArgumentException("Rol '" + roleName + "' niet gevonden.")))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        if (user.getStudent() != null && input.student != null) {
            Student s = user.getStudent();
            s.setFirstname(input.student.firstname);
            s.setLastname(input.student.lastname);
            s.setDefaultSlot(input.student.defaultSlot);
            studentRepo.save(s);
        }

        userRepo.save(user);
        return UserStudentDto.fromEntity(user);
    }

    public void updatePassword(String email, String newPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }
}