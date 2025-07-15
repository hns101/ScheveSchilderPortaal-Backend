package nl.scheveschilder.scheveschilderportaal.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkDto;
import nl.scheveschilder.scheveschilderportaal.dtos.GalleryDto;
import nl.scheveschilder.scheveschilderportaal.dtos.GalleryStatusDto;
import nl.scheveschilder.scheveschilderportaal.security.SecurityUtil;
import nl.scheveschilder.scheveschilderportaal.service.ArtworkPhotoService;
import nl.scheveschilder.scheveschilderportaal.service.ArtworkService;
import nl.scheveschilder.scheveschilderportaal.service.GalleryService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
public class GalleryController {

    private final GalleryService galleryService;
    private final ArtworkService artworkService;
    private final ArtworkPhotoService artworkPhotoService;
    private final SecurityUtil securityUtil;

    public GalleryController(
            GalleryService galleryService,
            ArtworkService artworkService,
            ArtworkPhotoService artworkPhotoService,
            SecurityUtil securityUtil
    ) {
        this.galleryService = galleryService;
        this.artworkService = artworkService;
        this.artworkPhotoService = artworkPhotoService;
        this.securityUtil = securityUtil;
    }

    // --- PUBLIC ENDPOINTS ---
    @GetMapping("/public/galleries")
    public ResponseEntity<List<GalleryDto>> getPublicGalleries() {
        List<GalleryDto> publicGalleries = galleryService.getPublicGalleries();
        return ResponseEntity.ok(publicGalleries);
    }

    @GetMapping("/public/gallery/{studentId}")
    public ResponseEntity<GalleryDto> getPublicGalleryById(@PathVariable Long studentId) {
        GalleryDto gallery = galleryService.getPublicGalleryByStudentId(studentId);
        return ResponseEntity.ok(gallery);
    }

    // --- NEW PUBLIC PHOTO ENDPOINT ---
    @GetMapping("/public/artworks/{id}/photo")
    public ResponseEntity<Resource> getPublicArtworkPhoto(@PathVariable Long id, HttpServletRequest request) throws MalformedURLException {
        // This service method contains the security check
        String photoFileName = artworkService.getPublicArtworkPhotoFileName(id);
        Resource resource = artworkPhotoService.getPhoto(photoFileName);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    // --- PROTECTED ENDPOINTS ---
    @GetMapping("/galleries/{email}")
    public ResponseEntity<GalleryDto> getGallery(@PathVariable String email) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(galleryService.getGalleryByStudentEmail(email));
    }

    @PutMapping("/galleries/{email}/status")
    public ResponseEntity<Void> updateGalleryStatus(@PathVariable String email, @Valid @RequestBody GalleryStatusDto statusDto) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        galleryService.updateGalleryStatus(email, statusDto.getIsPublic());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/galleries/{email}/artworks")
    public ResponseEntity<List<ArtworkDto>> getAllArtworks(@PathVariable String email) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(artworkService.getArtworksByStudentEmail(email));
    }

    @PostMapping("/galleries/{email}/artworks")
    public ResponseEntity<ArtworkDto> addArtwork(@PathVariable String email, @RequestBody ArtworkDto dto) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(artworkService.addArtworkToStudent(email, dto));
    }

    @DeleteMapping("/galleries/{email}/artworks/{artworkId}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable String email, @PathVariable Long artworkId) {
        if (!securityUtil.isSelfOrAdmin(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        artworkService.removeArtwork(email, artworkId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/artworks/{id}/photo")
    public ResponseEntity<Void> uploadArtworkPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        String fileName = artworkPhotoService.storePhoto(file);
        artworkService.assignPhotoToArtwork(id, fileName);
        return ResponseEntity.ok().build();
    }

    // This is the existing protected endpoint
    @GetMapping("/artworks/{id}/photo")
    public ResponseEntity<Resource> getArtworkPhoto(@PathVariable Long id, HttpServletRequest request) throws MalformedURLException {
        String photoFileName = artworkService.getArtworkPhotoFileName(id);
        Resource resource = artworkPhotoService.getPhoto(photoFileName);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
