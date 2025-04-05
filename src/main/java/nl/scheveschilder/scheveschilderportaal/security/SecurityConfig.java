package nl.scheveschilder.scheveschilderportaal.security;

import nl.scheveschilder.scheveschilderportaal.config.CustomCorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsUtils;

import java.util.List;

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
                        // ✅ allow CORS preflight requests early
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                        // ✅ all public endpoints first
                        .requestMatchers("/test").permitAll()
                        .requestMatchers(HttpMethod.POST, "/weeks").permitAll()
                        .requestMatchers(HttpMethod.GET, "/weeks").permitAll()
                        .requestMatchers(HttpMethod.GET, "/weeks/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/weeks/{weekId}/lessons/{lessonId}/students/{email}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/weeks/{weekId}/lessons/{lessonId}/students/{email}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/students").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register/admin/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register/admin/users/{email}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/register/admin/users/{email}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/register/admin/users/{email}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                        .requestMatchers("/auth/login").permitAll()

                        // ✅ this must be LAST
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
