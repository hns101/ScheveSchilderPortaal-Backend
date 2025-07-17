package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.service.ArtworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin") // Base path for all admin-specific endpoints
public class AdminController {

    private final ArtworkService artworkService;

    public AdminController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @DeleteMapping("/artworks/{artworkId}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable Long artworkId) {
        artworkService.adminDeleteArtwork(artworkId);
        return ResponseEntity.noContent().build();
    }
}
