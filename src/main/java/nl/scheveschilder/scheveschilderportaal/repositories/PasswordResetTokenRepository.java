package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.PasswordResetToken;
import nl.scheveschilder.scheveschilderportaal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for PasswordResetToken entities.
 * This extends JpaRepository to provide standard CRUD operations
 * and allows for custom query methods.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Finds a password reset token by its unique token string.
     *
     * @param token The token string to search for.
     * @return An Optional containing the found PasswordResetToken, or an empty Optional if not found.
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Finds a password reset token associated with a specific user.
     * This is useful to check if a user already has a pending reset request.
     *
     * @param user The user entity to search for.
     * @return An Optional containing the found PasswordResetToken, or an empty Optional if not found.
     */
    Optional<PasswordResetToken> findByUser(User user);

}