package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkDto;
import nl.scheveschilder.scheveschilderportaal.service.ArtworkPhotoService;
import nl.scheveschilder.scheveschilderportaal.service.ArtworkService;
import nl.scheveschilder.scheveschilderportaal.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin") // Base path for all admin-specific endpoints
public class AdminController {

    private final ArtworkService artworkService;
    private final GalleryService galleryService;
    private final ArtworkPhotoService artworkPhotoService;

    public AdminController(ArtworkService artworkService, GalleryService galleryService, ArtworkPhotoService artworkPhotoService) {
        this.artworkService = artworkService;
        this.galleryService = galleryService;
        this.artworkPhotoService = artworkPhotoService;
    }

    @DeleteMapping("/artworks/{artworkId}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable Long artworkId) {
        artworkService.adminDeleteArtwork(artworkId);
        return ResponseEntity.noContent().build();
    }

    // --- NEW: Endpoint for an admin to upload an artwork to a specific student's gallery ---
    @PostMapping("/galleries/{studentId}/artworks")
    public ResponseEntity<ArtworkDto> adminAddArtwork(
            @PathVariable Long studentId,
            @RequestParam("title") String title,
            @RequestParam("year") String year,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        // First, store the photo and get the generated filename
        String photoFileName = artworkPhotoService.storePhoto(file);

        // Create a DTO with the artwork details
        ArtworkDto artworkDto = new ArtworkDto();
        artworkDto.title = title;
        artworkDto.year = year;
        artworkDto.photoUrl = photoFileName; // Use the stored filename

        // Call the service to create the artwork record and link it to the student
        ArtworkDto createdArtwork = artworkService.adminAddArtworkToStudent(studentId, artworkDto);

        return new ResponseEntity<>(createdArtwork, HttpStatus.CREATED);
    }

    // --- NEW: Endpoint for an admin to set the cover photo for any gallery ---
    @PutMapping("/galleries/{studentId}/cover/{artworkId}")
    public ResponseEntity<Void> adminSetGalleryCoverPhoto(@PathVariable Long studentId, @PathVariable Long artworkId) {
        galleryService.adminSetGalleryCoverPhoto(studentId, artworkId);
        return ResponseEntity.noContent().build();
    }
}