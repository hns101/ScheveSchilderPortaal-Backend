package nl.scheveschilder.scheveschilderportaal.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long expectedId = 10L;

        // Act
        student.setId(expectedId);
        Long actualId = student.getId();

        // Assert
        assertEquals(expectedId, actualId, "getId should return the ID that was set");
    }

    @Test
    void testSetAndGetFirstname() {
        // Arrange
        String expectedFirstname = "Nico";

        // Act
        student.setFirstname(expectedFirstname);
        String actualFirstname = student.getFirstname();

        // Assert
        assertEquals(expectedFirstname, actualFirstname, "getFirstname should return the firstname that was set");
    }

    @Test
    void testSetAndGetLastname() {
        // Arrange
        String expectedLastname = "Heijnen";

        // Act
        student.setLastname(expectedLastname);
        String actualLastname = student.getLastname();

        // Assert
        assertEquals(expectedLastname, actualLastname, "getLastname should return the lastname that was set");
    }

    @Test
    void testSetAndGetDefaultSlot() {
        // Arrange
        String expectedDefaultSlot = "Vrijdag Avond";

        // Act
        student.setDefaultSlot(expectedDefaultSlot);
        String actualDefaultSlot = student.getDefaultSlot();

        // Assert
        assertEquals(expectedDefaultSlot, actualDefaultSlot, "getDefaultSlot should return the default slot that was set");
    }

    @Test
    void testSetAndIsActiveMember() {
        // Arrange
        boolean expectedActiveStatus = true;

        // Act
        student.setActiveMember(expectedActiveStatus);
        boolean actualActiveStatus = student.isActiveMember();

        // Assert
        assertTrue(actualActiveStatus, "isActiveMember should return true when active status is set to true");
    }

    @Test
    void testSetAndGetLessons() {
        // Arrange
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setSlot("Monday Morning");

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setSlot("Friday Evening");

        Set<Lesson> expectedLessons = new HashSet<>();
        expectedLessons.add(lesson1);
        expectedLessons.add(lesson2);

        // Act
        student.setLessons(expectedLessons);
        Set<Lesson> actualLessons = student.getLessons();

        // Assert
        assertEquals(2, actualLessons.size(), "getLessons should return the correct number of lessons");
        assertTrue(actualLessons.contains(lesson1), "The lessons set should contain the first lesson");
        assertTrue(actualLessons.contains(lesson2), "The lessons set should contain the second lesson");
    }

    @Test
    void testSetAndGetUser() {
        // Arrange
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");

        // Act
        student.setUser(expectedUser);
        User actualUser = student.getUser();

        // Assert
        assertEquals(expectedUser, actualUser, "getUser should return the user that was set");
        assertEquals("test@example.com", actualUser.getEmail(), "The user's email should be correct");
    }
}