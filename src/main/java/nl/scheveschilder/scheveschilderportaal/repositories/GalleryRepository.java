package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Optional<Gallery> findByStudent(Student student);

    // This will find all public galleries and sort them by the displayOrder field, ascending.
    List<Gallery> findAllByIsPublicTrueOrderByDisplayOrderAsc();
}
