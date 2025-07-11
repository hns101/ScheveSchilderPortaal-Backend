package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Role;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.models.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserStudentDto {
    public String email;
    public Set<String> roles;
    public StudentDto student; // optional

    public static UserStudentDto fromEntity(User user) {
        UserStudentDto dto = new UserStudentDto();
        dto.email = user.getEmail();
        dto.roles = user.getRoles().stream()
                .map(Role::getRolename)
                .collect(Collectors.toSet());

        if (user.getStudent() != null) {
            Student s = user.getStudent();
            StudentDto sdto = new StudentDto();
            sdto.id = s.getId();
            sdto.firstname = s.getFirstname();
            sdto.lastname = s.getLastname();
            sdto.defaultSlot = s.getDefaultSlot();
            sdto.email = user.getEmail(); // from linked user
            sdto.active = s.isActive(); // --- ADD THIS LINE ---
            dto.student = sdto;
        }

        return dto;
    }
}
