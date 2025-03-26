package nl.scheveschilder.scheveschilderportaal.repositories;


import nl.scheveschilder.scheveschilderportaal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
