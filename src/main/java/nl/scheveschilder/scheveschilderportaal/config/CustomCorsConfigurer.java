package nl.scheveschilder.scheveschilderportaal.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsUtils;

public class CustomCorsConfigurer extends AbstractHttpConfigurer<CustomCorsConfigurer, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
        );
    }
}