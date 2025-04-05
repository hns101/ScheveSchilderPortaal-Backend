package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.LessonDto;
import nl.scheveschilder.scheveschilderportaal.dtos.WeekDto;
import nl.scheveschilder.scheveschilderportaal.dtos.WeekResponseDto;
import nl.scheveschilder.scheveschilderportaal.models.Week;
import nl.scheveschilder.scheveschilderportaal.service.WeekService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weeks")
public class WeekController {

    private final WeekService weekService;

    public WeekController(WeekService weekService) {
        this.weekService = weekService;
    }

    @PostMapping
    public ResponseEntity<WeekResponseDto> createWeek(@RequestBody WeekDto dto) {
        Week created = weekService.createWeek(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(WeekResponseDto.fromEntity(created));
    }

    @GetMapping
    public ResponseEntity<List<WeekResponseDto>> getAllWeeks() {
        return ResponseEntity.ok(
                weekService.getAllWeeks().stream()
                        .map(WeekResponseDto::fromEntity)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeekResponseDto> getWeek(@PathVariable Long id) {
        return weekService.getWeekById(id)
                .map(WeekResponseDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeekResponseDto> updateWeek(@PathVariable Long id, @RequestBody WeekDto dto) {
        return ResponseEntity.ok(WeekResponseDto.fromEntity(weekService.updateWeek(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeek(@PathVariable Long id) {
        weekService.deleteWeek(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{weekId}/lessons/{lessonId}")
    public ResponseEntity<LessonDto> updateLessonInWeek(
            @PathVariable Long weekId,
            @PathVariable Long lessonId,
            @RequestBody LessonDto dto) {
        return ResponseEntity.ok(weekService.updateLesson(weekId, lessonId, dto));
    }

    @PutMapping("/{weekId}/lessons/{lessonId}/students")
    public ResponseEntity<LessonDto> updateLessonStudents(
            @PathVariable Long weekId,
            @PathVariable Long lessonId,
            @RequestBody List<String> studentIds) {
        return ResponseEntity.ok(weekService.updateLessonStudents(weekId, lessonId, studentIds));
    }

    @PostMapping("/{weekId}/lessons/{lessonId}/students/{email}")
    public ResponseEntity<LessonDto> addStudentToLessonByEmail(
            @PathVariable Long weekId,
            @PathVariable Long lessonId,
            @PathVariable String email) {
        return ResponseEntity.ok(weekService.addStudentByEmail(weekId, lessonId, email));
    }

    @DeleteMapping("/{weekId}/lessons/{lessonId}/students/{email}")
    public ResponseEntity<LessonDto> removeStudentFromLessonByEmail(
            @PathVariable Long weekId,
            @PathVariable Long lessonId,
            @PathVariable String email) {
        return ResponseEntity.ok(weekService.removeStudentByEmail(weekId, lessonId, email));
    }
}