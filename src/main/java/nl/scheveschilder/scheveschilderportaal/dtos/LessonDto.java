package nl.scheveschilder.scheveschilderportaal.dtos;

import java.time.LocalDate;
import java.util.List;

public class LessonDto {
    public String slot;
    public String time;
    public LocalDate date;
    public List<StudentDto> students;
}
