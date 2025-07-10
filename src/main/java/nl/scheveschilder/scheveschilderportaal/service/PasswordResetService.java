package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.exceptions.BadRequestException;
import nl.scheveschilder.scheveschilderportaal.exceptions.ResourceNotFoundException;
import nl.scheveschilder.scheveschilderportaal.models.PasswordResetToken;
import nl.scheveschilder.scheveschilderportaal.models.User;
import nl.scheveschilder.scheveschilderportaal.repositories.PasswordResetTokenRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles the "forgot password" request.
     * Generates a token and sends a reset email.
     * @param email The user's email address.
     */
    @Transactional
    public void handleForgotPassword(String email) {
        // Find user by email. Note: We don't throw an error if not found to prevent email enumeration attacks.
        userRepository.findByEmail(email).ifPresent(user -> {
            // If a token already exists for this user, delete it before creating a new one.
            tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

            String tokenString = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(tokenString, user);
            tokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(user.getEmail(), tokenString);
        });
    }

    /**
     * Handles the final "reset password" action.
     * Validates the token and updates the user's password.
     * @param tokenString The reset token.
     * @param newPassword The new password to set.
     */
    @Transactional
    public void handleResetPassword(String tokenString, String newPassword) {
        // Find the token in the database
        PasswordResetToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        // Check if the token has expired
        if (token.isExpired()) {
            tokenRepository.delete(token);
            throw new BadRequestException("Token has expired");
        }

        // Get the associated user and update their password
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the token so it cannot be used again
        tokenRepository.delete(token);
    }
}
