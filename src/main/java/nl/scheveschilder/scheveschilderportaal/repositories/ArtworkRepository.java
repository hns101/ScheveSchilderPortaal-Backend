package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.Artwork;
import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findByGallery(Gallery gallery);
}