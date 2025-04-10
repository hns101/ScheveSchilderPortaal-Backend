package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.security.JwtRequestFilter;
import nl.scheveschilder.scheveschilderportaal.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(StudentControllerTestConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    private StudentDto student1;
    private StudentDto student2;

    @BeforeEach
    void setUp() {
        // Arrange - Common test data setup
        student1 = new StudentDto();
        student1.id = 1L;
        student1.firstname = "Nico";
        student1.lastname = "Hoorn";
        student1.defaultSlot = "Vrijdag Avond";

        student2 = new StudentDto();
        student2.id = 2L;
        student2.firstname = "Lisa";
        student2.lastname = "Bakker";
        student2.defaultSlot = "Woensdag Avond";
    }

    @Test
    void getAllStudents_ShouldReturnList() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(List.of(student1, student2));

        // Act & Assert
        mockMvc.perform(get("/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("Nico"))
                .andExpect(jsonPath("$[1].lastname").value("Bakker"));
    }

    @Test
    void getStudentsFilteredBySlot_ShouldReturnFilteredList() throws Exception {
        // Arrange
        when(studentService.getStudentsFilteredBySlot("Vrijdag Avond")).thenReturn(List.of(student1));

        // Act & Assert
        mockMvc.perform(get("/students")
                        .param("slot", "Vrijdag Avond")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("Nico"));
    }
}