package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Optional<Gallery> findByStudentId(Long studentId);
}