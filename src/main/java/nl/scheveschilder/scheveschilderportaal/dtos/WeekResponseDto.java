package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Week;

import java.time.LocalDate;
import java.util.List;

public class WeekResponseDto {
    public Long id;
    public int weekNum;
    public LocalDate startDate;
    public List<LessonResponseDto> lessons;

    public static WeekResponseDto fromEntity(Week week) {
        WeekResponseDto dto = new WeekResponseDto();
        dto.id = week.getId();
        dto.weekNum = week.getweekNum();
        dto.startDate = week.getStartDate();
        dto.lessons = week.getLessons().stream()
                .map(LessonResponseDto::fromEntity)
                .toList();
        return dto;
    }
}