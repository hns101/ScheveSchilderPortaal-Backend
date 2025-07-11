package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.*;
import nl.scheveschilder.scheveschilderportaal.security.SecurityUtil;
import nl.scheveschilder.scheveschilderportaal.service.UserStudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserStudentController {

    private final UserStudentService userStudentService;
    private final SecurityUtil securityUtil;

    public UserStudentController(UserStudentService userStudentService, SecurityUtil securityUtil) {
        this.userStudentService = userStudentService;
        this.securityUtil = securityUtil;
    }

    // Unprotected for admin creation
    @PostMapping("/register")
    public ResponseEntity<StudentDto> registerStudent(@RequestBody RegisterStudentDto dto) {
        StudentDto created = userStudentService.registerUserAndStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserStudentDto>> getAllUsers() {
        return ResponseEntity.ok(userStudentService.getAllUsersWithStudents());
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserStudentDto> getUser(@PathVariable String email) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userStudentService.getUserByEmail(email));
    }

    @PutMapping("/users/{email}")
    public ResponseEntity<UserStudentDto> updateUser(@PathVariable String email, @RequestBody UserDto dto) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserStudentDto updated = userStudentService.updateUser(email, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userStudentService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{email}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String email, @RequestBody Map<String, String> body) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String newPassword = body.get("newPassword");
        userStudentService.updatePassword(email, newPassword);
        return ResponseEntity.noContent().build();
    }

    // --- NEW ENDPOINT ---
    @PutMapping("/users/{email}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable String email, @RequestBody UserStatusDto statusDto) {
        // Security for this should be handled in SecurityConfig to ensure only Admins can access.
        userStudentService.updateUserStatus(email, statusDto.getActive());
        return ResponseEntity.noContent().build();
    }
}