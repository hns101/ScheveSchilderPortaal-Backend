package nl.scheveschilder.scheveschilderportaal.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a password reset token sent to a user.
 * This token is temporary and allows the user to reset their password securely.
 */
@Entity
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok annotation for a no-argument constructor
@Table(name = "password_reset_tokens") // Specifies the table name in the database
public class PasswordResetToken {

    // The expiration time for the token, in minutes.
    private static final int EXPIRATION = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    // The user associated with this token.
    // FetchType.EAGER means the user data is loaded along with the token.
    // The foreign key column in the 'password_reset_tokens' table will be 'user_id'.
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Constructor to create a new PasswordResetToken for a given user and token string.
     * It automatically calculates the expiry date.
     *
     * @param token The unique token string.
     * @param user The user to whom this token belongs.
     */
    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    /**
     * Calculates the expiry date based on the current time and the defined expiration period.
     *
     * @return The calculated expiry date and time.
     */
    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION);
    }

    /**
     * Checks if the token has expired.
     *
     * @return true if the token's expiry date is before the current date and time, false otherwise.
     */
    public boolean isExpired() {
        return getExpiryDate().isBefore(LocalDateTime.now());
    }
}