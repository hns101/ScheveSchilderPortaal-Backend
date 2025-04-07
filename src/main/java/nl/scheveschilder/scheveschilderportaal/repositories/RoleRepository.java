package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}