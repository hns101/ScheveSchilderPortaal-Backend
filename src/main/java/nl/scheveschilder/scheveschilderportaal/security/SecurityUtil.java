package nl.scheveschilder.scheveschilderportaal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public boolean isSelfOrAdmin(String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if there is a valid, authenticated user session.
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return false;
        }

        // First, check if the user has the ADMIN role.
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true; // If they are an admin, always grant access.
        }

        // If not an admin, check if they are accessing their own resource.
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            // Compare the username from the security context with the email from the URL.
            return userDetails.getUsername().equalsIgnoreCase(email);
        }

        // Fallback for other principal types, though less common with JWT.
        if (principal instanceof String) {
            return ((String) principal).equalsIgnoreCase(email);
        }

        return false;
    }
}
