package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.UserStudentDto;
import nl.scheveschilder.scheveschilderportaal.service.UserStudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class UserStudentController {

    private final UserStudentService userStudentService;

    public UserStudentController(UserStudentService userStudentService) {
        this.userStudentService = userStudentService;
    }

    @PostMapping
    public ResponseEntity<StudentDto> register(@RequestBody StudentDto dto) {
        StudentDto created = userStudentService.createUserAndStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserStudentDto>> getAllUsers() {
        return ResponseEntity.ok(userStudentService.getAllUsersWithStudents());
    }

    @GetMapping("/admin/users/{email}")
    public ResponseEntity<UserStudentDto> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userStudentService.getUserByEmail(email));
    }

    @PutMapping("/admin/users/{email}")
    public ResponseEntity<UserStudentDto> updateUser(@PathVariable String email, @RequestBody UserStudentDto input) {
        UserStudentDto updated = userStudentService.updateUser(email, input);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/admin/users/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userStudentService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
