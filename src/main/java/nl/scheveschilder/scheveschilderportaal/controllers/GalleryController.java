package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkDto;
import nl.scheveschilder.scheveschilderportaal.dtos.GalleryDto;
import nl.scheveschilder.scheveschilderportaal.service.ArtworkService;
import nl.scheveschilder.scheveschilderportaal.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GalleryController {

    private final GalleryService galleryService;
    private final ArtworkService artworkService;

    public GalleryController(GalleryService galleryService, ArtworkService artworkService) {
        this.galleryService = galleryService;
        this.artworkService = artworkService;
    }

    @GetMapping("/galleries/{email}")
    public ResponseEntity<GalleryDto> getGallery(@PathVariable String email) {
        return ResponseEntity.ok(galleryService.getGalleryByStudentEmail(email));
    }


    @GetMapping("/galleries/{email}/artworks")
    public ResponseEntity<List<ArtworkDto>> getAllArtworks(@PathVariable String email) {
        return ResponseEntity.ok(artworkService.getArtworksByStudentEmail(email));
    }

    @PostMapping("/galleries/{email}/artworks")
    public ResponseEntity<ArtworkDto> addArtwork(@PathVariable String email, @RequestBody ArtworkDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artworkService.addArtworkToStudent(email, dto));
    }

    @DeleteMapping("/galleries/{email}/artworks/{artworkId}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable String email, @PathVariable Long artworkId) {
        artworkService.removeArtwork(email, artworkId);
        return ResponseEntity.noContent().build();
    }

}
