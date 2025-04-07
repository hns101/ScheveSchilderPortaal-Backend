package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Lesson;

import java.time.LocalDate;
import java.util.List;

public class LessonDto {
    public Long id;
    public String slot;
    public String time;
    public LocalDate date;
    public List<StudentDto> students;

    public static LessonDto fromEntity(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.id = lesson.getId();
        dto.slot = lesson.getSlot();
        dto.time = lesson.getTime();
        dto.date = lesson.getDate();
        dto.students = lesson.getStudents().stream().map(student -> {
            StudentDto s = new StudentDto();
            s.id = student.getId();
            s.firstname = student.getFirstname();
            s.lastname = student.getLastname();
            s.defaultSlot = student.getDefaultSlot();
            s.email = student.getUser() != null ? student.getUser().getEmail() : null;
            return s;
        }).toList();
        return dto;
    }
    public Lesson toEntity() {
        Lesson lesson = new Lesson();
        lesson.setId(this.id); // only if updating
        lesson.setSlot(this.slot);
        lesson.setTime(this.time);
        lesson.setDate(this.date); // if date = LocalDate, else parse
        return lesson;
    }
}