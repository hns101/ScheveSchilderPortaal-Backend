package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    // Finds all collections and sorts them by the displayOrder field
    List<Collection> findAllByOrderByDisplayOrderAsc();
}
