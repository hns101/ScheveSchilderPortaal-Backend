package nl.scheveschilder.scheveschilderportaal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service class for handling email sending operations.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a password reset email to the specified user.
     * This method is marked as @Async to run in a background thread,
     * so it doesn't block the main application flow.
     *
     * @param to The recipient's email address.
     * @param token The password reset token.
     */
    @Async
    public void sendPasswordResetEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@scheveschilder.nl"); // Or your 'from' address
            message.setTo(to);
            message.setSubject("Reset Your Password");

            // The URL should point to your React application's password reset page
            String resetUrl = "http://localhost:5173/reset-password?token=" + token; // Adjust the URL as needed

            String emailText = "To reset your password, click the link below:\n" + resetUrl
                    + "\n\nIf you did not request a password reset, please ignore this email.";

            message.setText(emailText);
            mailSender.send(message);
        } catch (Exception e) {
            // In a real application, you'd want more robust error handling/logging
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}