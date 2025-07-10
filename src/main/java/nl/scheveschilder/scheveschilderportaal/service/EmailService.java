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
            message.setSubject("Wachtwoord opnieuw instellen voor De Scheve Schilder 🎨");

            // The URL should point to your React application's password reset page
            String resetUrl = "http://localhost:5174/reset-password/" + token; // Pas de URL aan indien nodig

            // De creatieve Nederlandse e-mailtekst
            String emailText = "Hé kunstenaar! 👨‍🎨\n\n"
                    + "Ben je je wachtwoord vergeten? Geen paniek! Klik op de link hieronder om een nieuw wachtwoord te maken:\n\n"
                    + resetUrl + "\n\n"
                    + "Heb jij hier niet om gevraagd? Beschouw deze e-mail dan als een mislukt kunstwerk en negeer hem gerust. Je account is veilig!\n\n"
                    + "Met een kleurrijke groet,\n"
                    + "De Scheve Schilder 🖌️";

            message.setText(emailText);
            mailSender.send(message);
        } catch (Exception e) {
            // In a real application, you'd want more robust error handling/logging
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}