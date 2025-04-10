package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.service.StudentService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class StudentControllerTestConfig {
    @Bean
    public StudentService studentService() {
        return Mockito.mock(StudentService.class);
    }
}
