package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Student;

// This DTO is safe to use in public endpoints.
public class PublicStudentDto {
    public Long id;
    public String firstname;
    public String lastname; // This field contains the last name initial.

    public static PublicStudentDto fromEntity(Student student) {
        if (student == null) {
            return null;
        }
        var dto = new PublicStudentDto();
        dto.id = student.getId(); // --- ADD THIS LINE ---
        dto.firstname = student.getFirstname();

        if (student.getLastname() != null && !student.getLastname().isEmpty()) {
            dto.lastname = String.valueOf(student.getLastname().charAt(0));
        } else {
            dto.lastname = "";
        }

        return dto;
    }
}