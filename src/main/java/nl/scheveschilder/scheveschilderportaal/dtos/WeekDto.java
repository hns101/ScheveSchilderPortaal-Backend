package nl.scheveschilder.scheveschilderportaal.dtos;

import java.time.LocalDate;
import java.util.List;

public class WeekDto {
    public int weekNum;
    public LocalDate startDate;
    public List<LessonDto> lessons;
}
