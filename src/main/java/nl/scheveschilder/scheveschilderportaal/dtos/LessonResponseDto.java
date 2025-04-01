package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Lesson;

import java.time.LocalDate;
import java.util.List;

public class LessonResponseDto {
    public Long id;
    public String slot;
    public String time;
    public LocalDate date;
    public List<StudentDto> students;

    public static LessonResponseDto fromEntity(Lesson lesson) {
        LessonResponseDto dto = new LessonResponseDto();
        dto.id = lesson.getId();
        dto.slot = lesson.getSlot();
        dto.time = lesson.getTime();
        dto.date = lesson.getDate();
        dto.students = lesson.getStudents().stream()
                .map(student -> {
                    StudentDto s = new StudentDto();
                    s.id = student.getId();
                    s.firstname = student.getFirstname();
                    s.lastname = student.getLastname();
                    s.defaultSlot = student.getDefaultSlot();
                    return s;
                }).toList();
        return dto;
    }
}
