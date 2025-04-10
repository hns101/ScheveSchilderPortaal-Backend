package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.LessonDto;
import nl.scheveschilder.scheveschilderportaal.dtos.WeekDto;
import nl.scheveschilder.scheveschilderportaal.dtos.WeekResponseDto;
import nl.scheveschilder.scheveschilderportaal.models.Lesson;
import nl.scheveschilder.scheveschilderportaal.models.Week;
import nl.scheveschilder.scheveschilderportaal.service.WeekService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WeekControllerTest {

    @Mock
    private WeekService weekService;

    @InjectMocks
    private WeekController weekController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWeek() {
        // Arrange
        WeekDto inputDto = new WeekDto();
        inputDto.weekNum = 15;
        inputDto.startDate = LocalDate.now();
        inputDto.lessons = new ArrayList<>();

        Week createdWeek = new Week();
        createdWeek.setId(1L);
        createdWeek.setweekNum(15);
        createdWeek.setStartDate(inputDto.startDate);

        when(weekService.createWeek(any(WeekDto.class))).thenReturn(createdWeek);

        // Act
        ResponseEntity<WeekResponseDto> response = weekController.createWeek(inputDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1L, response.getBody().id, "Week ID should match");
        assertEquals(15, response.getBody().weekNum, "Week number should match");
        verify(weekService, times(1)).createWeek(any(WeekDto.class));
    }

    @Test
    void getAllWeeks() {
        // Arrange
        Week week1 = new Week();
        week1.setId(1L);
        week1.setweekNum(10);

        Week week2 = new Week();
        week2.setId(2L);
        week2.setweekNum(11);

        List<Week> weekList = List.of(week1, week2);
        when(weekService.getAllWeeks()).thenReturn(weekList);

        // Act
        ResponseEntity<List<WeekResponseDto>> response = weekController.getAllWeeks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(2, response.getBody().size(), "Should return 2 weeks");
        assertEquals(1L, response.getBody().get(0).id, "First week ID should match");
        assertEquals(2L, response.getBody().get(1).id, "Second week ID should match");
        verify(weekService, times(1)).getAllWeeks();
    }

    @Test
    void getWeek_whenFound() {
        // Arrange
        Long weekId = 5L;
        Week week = new Week();
        week.setId(weekId);
        week.setweekNum(25);

        when(weekService.getWeekById(weekId)).thenReturn(Optional.of(week));

        // Act
        ResponseEntity<WeekResponseDto> response = weekController.getWeek(weekId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(weekId, response.getBody().id, "Week ID should match");
        assertEquals(25, response.getBody().weekNum, "Week number should match");
        verify(weekService, times(1)).getWeekById(weekId);
    }

    @Test
    void getWeek_whenNotFound() {
        // Arrange
        Long weekId = 999L;
        when(weekService.getWeekById(weekId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<WeekResponseDto> response = weekController.getWeek(weekId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(weekService, times(1)).getWeekById(weekId);
    }

    @Test
    void updateWeek() {
        // Arrange
        Long weekId = 10L;
        WeekDto inputDto = new WeekDto();
        inputDto.weekNum = 30;
        inputDto.startDate = LocalDate.now();

        Week updatedWeek = new Week();
        updatedWeek.setId(weekId);
        updatedWeek.setweekNum(30);
        updatedWeek.setStartDate(inputDto.startDate);

        when(weekService.updateWeek(eq(weekId), any(WeekDto.class))).thenReturn(updatedWeek);

        // Act
        ResponseEntity<WeekResponseDto> response = weekController.updateWeek(weekId, inputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(weekId, response.getBody().id, "Week ID should match");
        assertEquals(30, response.getBody().weekNum, "Updated week number should match");
        verify(weekService, times(1)).updateWeek(eq(weekId), any(WeekDto.class));
    }

    @Test
    void deleteWeek() {
        // Arrange
        Long weekId = 15L;
        doNothing().when(weekService).deleteWeek(weekId);

        // Act
        ResponseEntity<Void> response = weekController.deleteWeek(weekId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status code should be NO_CONTENT");
        assertNull(response.getBody(), "Response body should be null");
        verify(weekService, times(1)).deleteWeek(weekId);
    }

    @Test
    void updateLessonInWeek() {
        // Arrange
        Long weekId = 20L;
        Long lessonId = 5L;

        LessonDto inputDto = new LessonDto();
        inputDto.slot = "Maandag Ochtend";
        inputDto.time = "10:00";
        inputDto.date = LocalDate.now();

        LessonDto updatedDto = new LessonDto();
        updatedDto.id = lessonId;
        updatedDto.slot = inputDto.slot;
        updatedDto.time = inputDto.time;
        updatedDto.date = inputDto.date;

        when(weekService.updateLesson(weekId, lessonId, inputDto)).thenReturn(updatedDto);

        // Act
        ResponseEntity<LessonDto> response = weekController.updateLessonInWeek(weekId, lessonId, inputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(lessonId, response.getBody().id, "Lesson ID should match");
        assertEquals(inputDto.slot, response.getBody().slot, "Lesson slot should match");
        assertEquals(inputDto.time, response.getBody().time, "Lesson time should match");
        verify(weekService, times(1)).updateLesson(weekId, lessonId, inputDto);
    }

    @Test
    void updateLessonStudents() {
        // Arrange
        Long weekId = 25L;
        Long lessonId = 8L;
        List<String> studentIds = List.of("1", "2", "3");

        LessonDto updatedDto = new LessonDto();
        updatedDto.id = lessonId;
        updatedDto.students = new ArrayList<>();

        when(weekService.updateLessonStudents(weekId, lessonId, studentIds)).thenReturn(updatedDto);

        // Act
        ResponseEntity<LessonDto> response = weekController.updateLessonStudents(weekId, lessonId, studentIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(lessonId, response.getBody().id, "Lesson ID should match");
        verify(weekService, times(1)).updateLessonStudents(weekId, lessonId, studentIds);
    }

    @Test
    void addStudentToLessonByEmail() {
        // Arrange
        Long weekId = 30L;
        Long lessonId = 12L;
        String email = "student@example.com";

        LessonDto updatedDto = new LessonDto();
        updatedDto.id = lessonId;

        when(weekService.addStudentByEmail(weekId, lessonId, email)).thenReturn(updatedDto);

        // Act
        ResponseEntity<LessonDto> response = weekController.addStudentToLessonByEmail(weekId, lessonId, email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(lessonId, response.getBody().id, "Lesson ID should match");
        verify(weekService, times(1)).addStudentByEmail(weekId, lessonId, email);
    }

    @Test
    void removeStudentFromLessonByEmail() {
        // Arrange
        Long weekId = 35L;
        Long lessonId = 15L;
        String email = "student@example.com";

        LessonDto updatedDto = new LessonDto();
        updatedDto.id = lessonId;

        when(weekService.removeStudentByEmail(weekId, lessonId, email)).thenReturn(updatedDto);

        // Act
        ResponseEntity<LessonDto> response = weekController.removeStudentFromLessonByEmail(weekId, lessonId, email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(lessonId, response.getBody().id, "Lesson ID should match");
        verify(weekService, times(1)).removeStudentByEmail(weekId, lessonId, email);
    }
}