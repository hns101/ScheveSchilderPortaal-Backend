package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.models.User;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import nl.scheveschilder.scheveschilderportaal.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepo;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setEmail("nico@example.com");

        User user2 = new User();
        user2.setEmail("lisa@example.com");

        student1 = new Student();
        student1.setId(1L);
        student1.setFirstname("Nico");
        student1.setLastname("Heijnen");
        student1.setDefaultSlot("Vrijdag Avond");
        student1.setUser(user1);

        student2 = new Student();
        student2.setId(2L);
        student2.setFirstname("Lisa");
        student2.setLastname("Bakker");
        student2.setDefaultSlot("Woensdag Avond");
        student2.setUser(user2);
    }

    @Test
    void getAllStudents_ShouldReturnAllDtos() {
        when(studentRepo.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<StudentDto> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("Nico", result.get(0).firstname);
        assertEquals("Lisa", result.get(1).firstname);
    }

    @Test
    void getStudentsFilteredBySlot_ShouldFilterCorrectly() {
        when(studentRepo.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<StudentDto> result = studentService.getStudentsFilteredBySlot("Vrijdag Avond");

        assertEquals(1, result.size());
        assertEquals("Nico", result.getFirst().firstname);
    }

    @Test
    void getStudentsFilteredBySlot_ShouldReturnAllIfSlotIsNull() {
        when(studentRepo.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<StudentDto> result = studentService.getStudentsFilteredBySlot(null);

        assertEquals(2, result.size());
    }
}
