package nl.scheveschilder.scheveschilderportaal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync; // Import this

@SpringBootApplication
@EnableAsync // Add this annotation
public class BackendPortaalScheveschilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendPortaalScheveschilderApplication.class, args);
    }

}

