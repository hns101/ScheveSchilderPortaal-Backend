package nl.scheveschilder.scheveschilderportaal.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LessonTest {

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        lesson = new Lesson();
    }

    @Test
    void getId() {
        // Arrange
        Long expectedId = 1L;
        lesson.setId(expectedId);

        // Act
        Long actualId = lesson.getId();

        // Assert
        assertEquals(expectedId, actualId, "getId should return the ID that was set");
    }

    @Test
    void setId() {
        // Arrange
        Long expectedId = 5L;

        // Act
        lesson.setId(expectedId);

        // Assert
        assertEquals(expectedId, lesson.getId(), "setId should correctly set the ID value");
    }

    @Test
    void getSlot() {
        // Arrange
        String expectedSlot = "Vrijdag Avond";
        lesson.setSlot(expectedSlot);

        // Act
        String actualSlot = lesson.getSlot();

        // Assert
        assertEquals(expectedSlot, actualSlot, "getSlot should return the slot that was set");
    }

    @Test
    void setSlot() {
        // Arrange
        String expectedSlot = "Maandag Ochtend";

        // Act
        lesson.setSlot(expectedSlot);

        // Assert
        assertEquals(expectedSlot, lesson.getSlot(), "setSlot should correctly set the slot value");
    }

    @Test
    void getTime() {
        // Arrange
        String expectedTime = "19:00 - 21:30";
        lesson.setTime(expectedTime);

        // Act
        String actualTime = lesson.getTime();

        // Assert
        assertEquals(expectedTime, actualTime, "getTime should return the time that was set");
    }

    @Test
    void setTime() {
        // Arrange
        String expectedTime = "09:00 - 11:30";

        // Act
        lesson.setTime(expectedTime);

        // Assert
        assertEquals(expectedTime, lesson.getTime(), "setTime should correctly set the time value");
    }

    @Test
    void getDate() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2023, 10, 20);
        lesson.setDate(expectedDate);

        // Act
        LocalDate actualDate = lesson.getDate();

        // Assert
        assertEquals(expectedDate, actualDate, "getDate should return the date that was set");
    }

    @Test
    void setDate() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2023, 11, 5);

        // Act
        lesson.setDate(expectedDate);

        // Assert
        assertEquals(expectedDate, lesson.getDate(), "setDate should correctly set the date value");
    }

    @Test
    void getWeek() {
        // Arrange
        Week expectedWeek = new Week();
        expectedWeek.setId(10L);
        expectedWeek.setweekNum(42);
        lesson.setWeek(expectedWeek);

        // Act
        Week actualWeek = lesson.getWeek();

        // Assert
        assertEquals(expectedWeek, actualWeek, "getWeek should return the week that was set");
        assertEquals(10L, actualWeek.getId(), "The week's ID should be correct");
        assertEquals(42, actualWeek.getweekNum(), "The week's number should be correct");
    }

    @Test
    void setWeek() {
        // Arrange
        Week expectedWeek = new Week();
        expectedWeek.setId(15L);
        expectedWeek.setweekNum(35);

        // Act
        lesson.setWeek(expectedWeek);

        // Assert
        assertEquals(expectedWeek, lesson.getWeek(), "setWeek should correctly set the week value");
        assertEquals(15L, lesson.getWeek().getId(), "The week's ID should be set correctly");
    }

    @Test
    void getStudents() {
        // Arrange
        Set<Student> expectedStudents = new HashSet<>();
        Student student1 = new Student();
        student1.setId(1L);
        User user1 = new User();
        user1.setEmail("student1@example.com");
        student1.setUser(user1);

        Student student2 = new Student();
        student2.setId(2L);
        User user2 = new User();
        user2.setEmail("student2@example.com");
        student2.setUser(user2);

        expectedStudents.add(student1);
        expectedStudents.add(student2);
        lesson.setStudents(expectedStudents);

        // Act
        Set<Student> actualStudents = lesson.getStudents();

        // Assert
        assertEquals(expectedStudents.size(), actualStudents.size(), "getStudents should return the correct number of students");
        assertEquals(expectedStudents, actualStudents, "getStudents should return the set of students that was set");
        assertTrue(actualStudents.contains(student1), "The student set should contain the first student");
        assertTrue(actualStudents.contains(student2), "The student set should contain the second student");
    }

    @Test
    void setStudents() {
        // Arrange
        Set<Student> expectedStudents = new HashSet<>();
        Student student = new Student();
        student.setId(3L);
        User user = new User();
        user.setEmail("student3@example.com");
        student.setUser(user);
        expectedStudents.add(student);

        // Act
        lesson.setStudents(expectedStudents);

        // Assert
        assertEquals(expectedStudents, lesson.getStudents(), "setStudents should correctly set the set of students");
        assertEquals(1, lesson.getStudents().size(), "The set should contain the correct number of students");
        assertTrue(lesson.getStudents().contains(student), "The set should contain the added student");
    }

    @Test
    void defaultConstructorShouldInitializeEmptyStudentsSet() {
        // Act
        Lesson newLesson = new Lesson();

        // Assert
        assertNotNull(newLesson.getStudents(), "The students set should be initialized and not null");
        assertTrue(newLesson.getStudents().isEmpty(), "The students set should be empty by default");
    }

    @Test
    void addStudent() {
        // Arrange
        Student student = new Student();
        student.setId(4L);
        User user = new User();
        user.setEmail("new@example.com");
        student.setUser(user);

        // Act
        lesson.getStudents().add(student);

        // Assert
        assertEquals(1, lesson.getStudents().size(), "The student should be added to the set");
        assertTrue(lesson.getStudents().contains(student), "The set should contain the added student");
    }

    @Test
    void removeStudent() {
        // Arrange
        Student student = new Student();
        student.setId(5L);
        User user = new User();
        user.setEmail("remove@example.com");
        student.setUser(user);
        lesson.getStudents().add(student);

        // Act
        lesson.getStudents().remove(student);

        // Assert
        assertEquals(0, lesson.getStudents().size(), "The student should be removed from the set");
        assertFalse(lesson.getStudents().contains(student), "The set should not contain the removed student");
    }
}