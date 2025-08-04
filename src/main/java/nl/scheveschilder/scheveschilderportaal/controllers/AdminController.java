package nl.scheveschilder.scheveschilderportaal.controllers;

import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkDto;
import nl.scheveschilder.scheveschilderportaal.dtos.GalleryDto;
import nl.scheveschilder.scheveschilderportaal.dtos.GalleryStatusDto;
import nl.scheveschilder.scheveschilderportaal.service.ArtworkPhotoService;
import nl.scheveschilder.scheveschilderportaal.service.ArtworkService;
import nl.scheveschilder.scheveschilderportaal.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ArtworkService artworkService;
    private final GalleryService galleryService;
    private final ArtworkPhotoService artworkPhotoService;

    public AdminController(ArtworkService artworkService, GalleryService galleryService, ArtworkPhotoService artworkPhotoService) {
        this.artworkService = artworkService;
        this.galleryService = galleryService;
        this.artworkPhotoService = artworkPhotoService;
    }

    // --- NEW: Endpoint to get ALL galleries for the admin manager ---
    @GetMapping("/galleries")
    public ResponseEntity<List<GalleryDto>> getAllGalleries() {
        return ResponseEntity.ok(galleryService.adminGetAllGalleries());
    }

    // --- NEW: Endpoint for an admin to update any gallery's status ---
    @PutMapping("/galleries/{galleryId}/status")
    public ResponseEntity<Void> updateGalleryStatus(@PathVariable Long galleryId, @RequestBody GalleryStatusDto statusDto) {
        galleryService.adminUpdateGalleryStatus(galleryId, statusDto.getIsPublic());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/artworks/{artworkId}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable Long artworkId) {
        artworkService.adminDeleteArtwork(artworkId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/galleries/{studentId}/artworks")
    public ResponseEntity<ArtworkDto> adminAddArtwork(
            @PathVariable Long studentId,
            @RequestParam("title") String title,
            @RequestParam("year") String year,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String photoFileName = artworkPhotoService.storePhoto(file);
        ArtworkDto artworkDto = new ArtworkDto();
        artworkDto.title = title;
        artworkDto.year = year;
        artworkDto.photoUrl = photoFileName;
        ArtworkDto createdArtwork = artworkService.adminAddArtworkToStudent(studentId, artworkDto);
        return new ResponseEntity<>(createdArtwork, HttpStatus.CREATED);
    }

    @PutMapping("/galleries/{studentId}/cover/{artworkId}")
    public ResponseEntity<Void> adminSetGalleryCoverPhoto(@PathVariable Long studentId, @PathVariable Long artworkId) {
        galleryService.adminSetGalleryCoverPhoto(studentId, artworkId);
        return ResponseEntity.noContent().build();
    }
}
