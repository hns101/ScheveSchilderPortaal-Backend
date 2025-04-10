package nl.scheveschilder.scheveschilderportaal.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeekTest {

    private Week week;

    @BeforeEach
    void setUp() {
        week = new Week();
    }

    @Test
    void getId() {
        // Arrange
        Long expectedId = 1L;
        week.setId(expectedId);

        // Act
        Long actualId = week.getId();

        // Assert
        assertEquals(expectedId, actualId, "getId should return the ID that was set");
    }

    @Test
    void setId() {
        // Arrange
        Long expectedId = 5L;

        // Act
        week.setId(expectedId);

        // Assert
        assertEquals(expectedId, week.getId(), "setId should correctly set the ID value");
    }

    @Test
    void getweekNum() {
        // Arrange
        int expectedWeekNum = 42;
        week.setweekNum(expectedWeekNum);

        // Act
        int actualWeekNum = week.getweekNum();

        // Assert
        assertEquals(expectedWeekNum, actualWeekNum, "getweekNum should return the week number that was set");
    }

    @Test
    void setweekNum() {
        // Arrange
        int expectedWeekNum = 15;

        // Act
        week.setweekNum(expectedWeekNum);

        // Assert
        assertEquals(expectedWeekNum, week.getweekNum(), "setweekNum should correctly set the week number");
    }

    @Test
    void getStartDate() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2023, 10, 15);
        week.setStartDate(expectedDate);

        // Act
        LocalDate actualDate = week.getStartDate();

        // Assert
        assertEquals(expectedDate, actualDate, "getStartDate should return the start date that was set");
    }

    @Test
    void setStartDate() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2023, 11, 1);

        // Act
        week.setStartDate(expectedDate);

        // Assert
        assertEquals(expectedDate, week.getStartDate(), "setStartDate should correctly set the start date");
    }

    @Test
    void getLessons() {
        // Arrange
        List<Lesson> expectedLessons = new ArrayList<>();
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        expectedLessons.add(lesson1);
        expectedLessons.add(lesson2);
        week.setLessons(expectedLessons);

        // Act
        List<Lesson> actualLessons = week.getLessons();

        // Assert
        assertEquals(expectedLessons.size(), actualLessons.size(), "getLessons should return the correct number of lessons");
        assertEquals(expectedLessons, actualLessons, "getLessons should return the list of lessons that was set");
    }

    @Test
    void setLessons() {
        // Arrange
        List<Lesson> expectedLessons = new ArrayList<>();
        Lesson lesson = new Lesson();
        lesson.setId(3L);
        lesson.setSlot("Monday Morning");
        expectedLessons.add(lesson);

        // Act
        week.setLessons(expectedLessons);

        // Assert
        assertEquals(expectedLessons, week.getLessons(), "setLessons should correctly set the list of lessons");
        assertEquals(1, week.getLessons().size(), "The list should contain the correct number of lessons");
        assertEquals("Monday Morning", week.getLessons().get(0).getSlot(), "The lesson in the list should have the correct properties");
    }

    @Test
    void defaultConstructorShouldInitializeEmptyLessonsList() {
        // Act
        Week newWeek = new Week();

        // Assert
        assertNotNull(newWeek.getLessons(), "The lessons list should be initialized and not null");
        assertTrue(newWeek.getLessons().isEmpty(), "The lessons list should be empty by default");
    }
}