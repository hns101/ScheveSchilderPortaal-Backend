package nl.scheveschilder.scheveschilderportaal.repositories;


import nl.scheveschilder.scheveschilderportaal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email); // ✅ Add this line

}