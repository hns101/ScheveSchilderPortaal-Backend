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
import org.springframework.web.cors.CorsUtils;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ Group all public endpoints together at the top
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/galleries").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/gallery/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/artworks/**").permitAll()

                        // Role-based security for other endpoints
                        .requestMatchers(HttpMethod.POST, "/weeks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/weeks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/weeks/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/weeks/{id}").hasAnyRole( "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/weeks/{weekId}/lessons/{lessonId}/students/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/weeks/{weekId}/lessons/{lessonId}/students/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{email}/password").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{email}/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{email}").hasAnyRole( "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/students").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/galleries/{email}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/galleries/{email}/status").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/galleries/{email}/artworks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/galleries/{email}/artworks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/galleries/{email}/artworks/{artworkId}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/artworks/{id}/photo").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/artworks/{id}/photo").hasAnyRole("USER", "ADMIN")

                        // ✅ This must be LAST to secure all other endpoints
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}