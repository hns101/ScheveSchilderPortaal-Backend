package nl.scheveschilder.scheveschilderportaal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SecurityUtil {

    public boolean isSelfOrAdmin(String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // --- START DEBUG LOGGING ---
        System.out.println("--- SecurityUtil Check ---");
        System.out.println("Target Email: " + email);

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            System.out.println("Result: FAILED (No authentication found)");
            return false;
        }

        String principalName = auth.getName();
        String authorities = auth.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(", "));

        System.out.println("Authenticated Principal: " + principalName);
        System.out.println("Authorities: [" + authorities + "]");
        // --- END DEBUG LOGGING ---


        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            System.out.println("Result: SUCCESS (User is an admin)");
            return true;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            boolean isSelf = userDetails.getUsername().equalsIgnoreCase(email);
            System.out.println("Result: " + (isSelf ? "SUCCESS (Is self)" : "FAILED (Not self)"));
            return isSelf;
        }

        System.out.println("Result: FAILED (Principal is not an instance of UserDetails)");
        return false;
    }
}