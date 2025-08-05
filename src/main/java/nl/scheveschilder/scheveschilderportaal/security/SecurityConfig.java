package nl.scheveschilder.scheveschilderportaal.security;

import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SecurityConfig(JwtService service, UserRepository userRepos) {
        this.jwtService = service;
        this.userRepository = userRepos;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService udService, PasswordEncoder passwordEncoder) {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(udService);
        return new ProviderManager(auth);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(this.userRepository);
    }

    // --- NEW: CORS Configuration Bean ---
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from your React app's origin
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173", "http://localhost:5174"));
        // Allow all standard methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow all headers, including the Authorization header
        configuration.setAllowedHeaders(List.of("*"));
        // Allow credentials
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // --- NEW: Apply the CORS configuration ---
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow all OPTIONS requests
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll()


                        // Admin endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // User/Admin endpoints
                        .requestMatchers(HttpMethod.POST, "/weeks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/weeks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/weeks/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/weeks/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/weeks/{weekId}/lessons/{lessonId}/students/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/weeks/{weekId}/lessons/{lessonId}/students/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{email}/password").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{email}/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{email}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/students").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/galleries/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/galleries/{email}/status").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/galleries/{email}/cover/{artworkId}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/galleries/{email}/artworks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/galleries/{email}/artworks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/galleries/{email}/artworks/{artworkId}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/artworks/{id}/photo").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/artworks/{id}/photo").hasAnyRole("USER", "ADMIN")

                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}