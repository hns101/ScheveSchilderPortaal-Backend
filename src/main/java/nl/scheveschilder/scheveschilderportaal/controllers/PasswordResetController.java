package nl.scheveschilder.scheveschilderportaal.controllers;

import jakarta.validation.Valid;
import nl.scheveschilder.scheveschilderportaal.dtos.PasswordResetDto;
import nl.scheveschilder.scheveschilderportaal.dtos.PasswordResetRequestDto;
import nl.scheveschilder.scheveschilderportaal.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // Optional: Add if you get CORS errors from your frontend
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    /**
     * Endpoint to request a password reset email.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordResetRequestDto resetRequest) {
        passwordResetService.handleForgotPassword(resetRequest.getEmail());
        // Always return a generic success message for security reasons.
        String message = "Als er een account met dat e-mailadres bestaat, is er een link verzonden om uw wachtwoord opnieuw in te stellen.";
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint to set a new password using a valid token.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto) {
        passwordResetService.handleResetPassword(passwordResetDto.getToken(), passwordResetDto.getNewPassword());
        return ResponseEntity.ok("Uw wachtwoord is succesvol bijgewerkt.");
    }
}