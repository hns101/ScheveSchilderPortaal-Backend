package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.UserStudentDto;
import nl.scheveschilder.scheveschilderportaal.models.Role;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.models.User;
import nl.scheveschilder.scheveschilderportaal.repositories.RoleRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserStudentService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public UserStudentService(StudentRepository studentRepo, UserRepository userRepo, RoleRepository roleRepo) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public StudentDto createUserAndStudent(StudentDto dto) {
        if (studentRepo.existsById(dto.id)) {
            throw new IllegalArgumentException("Student ID '" + dto.id + "' bestaat al.");
        }

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
        student.setId(dto.id);
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
}