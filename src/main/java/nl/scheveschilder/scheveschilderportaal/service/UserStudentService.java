package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.UserDto;
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

import java.util.Arrays;
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
            PasswordEncoder passwordEncoder
    ) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentDto createUserAndStudent(StudentDto dto) {
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

            student.getLessons().forEach(lesson -> lesson.getStudents().remove(student));
            studentRepo.save(student);

            studentRepo.delete(student);
        }

        userRepo.delete(user);
    }

    public UserStudentDto updateUser(String email, UserDto input) {
        User user = userRepo.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker met email '" + email + "' niet gevonden."));

        // Skip updating email — it's immutable here

        if (input.roles != null && input.roles.length > 0) {
            Set<Role> roles = Arrays.stream(input.roles)
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
