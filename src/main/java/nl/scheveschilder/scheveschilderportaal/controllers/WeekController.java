package nl.scheveschilder.scheveschilderportaal.controllers;

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
        WeekResponseDto response = WeekResponseDto.fromEntity(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WeekResponseDto>> getAllWeeks() {
        List<WeekResponseDto> result = weekService.getAllWeeks().stream()
                .map(WeekResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeekResponseDto> getWeek(@PathVariable Long id) {
        return weekService.getWeekById(id)
                .map(WeekResponseDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
