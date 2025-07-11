package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.RegisterStudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.UserDto;
import nl.scheveschilder.scheveschilderportaal.dtos.UserStudentDto;
import nl.scheveschilder.scheveschilderportaal.exceptions.ResourceNotFoundException;
import nl.scheveschilder.scheveschilderportaal.models.Lesson;
import nl.scheveschilder.scheveschilderportaal.models.Role;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.models.User;
import nl.scheveschilder.scheveschilderportaal.repositories.RoleRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
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
    public StudentDto registerUserAndStudent(RegisterStudentDto dto) {
        if (dto.password == null || dto.password.length() < 6) {
            throw new IllegalArgumentException("Wachtwoord moet minimaal 6 tekens lang zijn.");
        }

        User user = new User();
        user.setEmail(dto.email);
        user.setPassword(passwordEncoder.encode(dto.password));

        Role roleUser = roleRepo.findById("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Rol 'ROLE_USER' niet gevonden."));
        user.setRoles(Set.of(roleUser));

        userRepo.save(user);

        Student student = new Student();
        student.setFirstname(dto.firstname);
        student.setLastname(dto.lastname);
        student.setDefaultSlot(dto.defaultSlot);
        student.setActive(true); // Use the new field and method
        student.setUser(user);

        studentRepo.save(student);

        // Convert to StudentDto
        StudentDto result = new StudentDto();
        result.id = student.getId();
        result.firstname = student.getFirstname();
        result.lastname = student.getLastname();
        result.defaultSlot = student.getDefaultSlot();
        result.email = user.getEmail();

        return result;
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

    // --- NEW METHOD ---
    @Transactional // Ensures all database operations in this method succeed or fail together
    public void updateUserStatus(String email, boolean isActive) {
        User user = userRepo.findById(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email '" + email + "' not found."));

        Student student = user.getStudent();
        if (student == null) {
            throw new ResourceNotFoundException("No student profile found for user with email '" + email + "'.");
        }

        // If deactivating the user, perform the cleanup actions
        if (!isActive) {
            // Create a copy of the set to avoid modification issues while iterating
            Set<Lesson> lessonsToRemoveFrom = new HashSet<>(student.getLessons());
            for (Lesson lesson : lessonsToRemoveFrom) {
                lesson.getStudents().remove(student);
            }
            // Clear the student's side of the relationship
            student.getLessons().clear();

            // Set default slot to null
            student.setDefaultSlot(null);
        }

        // Update the active status
        student.setActive(isActive);

        // Save the updated student profile.
        // Due to @Transactional, changes to lessons will also be persisted.
        studentRepo.save(student);
    }
}