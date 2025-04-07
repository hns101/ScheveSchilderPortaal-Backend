package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepo;

    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public List<StudentDto> getAllStudents() {
        return studentRepo.findAll().stream()
                .map(student -> {
                    StudentDto dto = new StudentDto();
                    dto.id = student.getId();
                    dto.firstname = student.getFirstname();
                    dto.lastname = student.getLastname();
                    dto.defaultSlot = student.getDefaultSlot();
                    dto.email = student.getUser() != null ? student.getUser().getEmail() : null;
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<StudentDto> getStudentsFilteredBySlot(String slot) {
        return studentRepo.findAll().stream()
                .filter(s -> slot == null || s.getDefaultSlot().equalsIgnoreCase(slot))
                .map(student -> {
                    StudentDto dto = new StudentDto();
                    dto.id = student.getId();
                    dto.firstname = student.getFirstname();
                    dto.lastname = student.getLastname();
                    dto.defaultSlot = student.getDefaultSlot();
                    dto.email = student.getUser() != null ? student.getUser().getEmail() : null;
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
