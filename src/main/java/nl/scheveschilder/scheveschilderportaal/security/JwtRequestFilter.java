package nl.scheveschilder.scheveschilderportaal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    public JwtRequestFilter(JwtService jwtService, UserDetailsService udService) {
        this.jwtService = jwtService;
        this.userDetailsService = udService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest
                                            request,
                                    @NonNull HttpServletResponse
                                            response,
                                    @NonNull FilterChain
                                            filterChain) throws ServletException, IOException {
        final String authorizationHeader =
                request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if (authorizationHeader != null &&
                authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtService.extractUsername(jwt);
        }
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails =
                    this.userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken
                        usernamePasswordAuthenticationToken = new
                            UsernamePasswordAuthenticationToken(
                            userDetails, null,
                            userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
