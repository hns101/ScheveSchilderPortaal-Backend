package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.GalleryDto;
import nl.scheveschilder.scheveschilderportaal.exceptions.ResourceNotFoundException;
import nl.scheveschilder.scheveschilderportaal.models.Artwork;
import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.repositories.ArtworkRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.GalleryRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GalleryService {

    private final GalleryRepository galleryRepo;
    private final StudentRepository studentRepo;
    private final ArtworkRepository artworkRepo; // Add ArtworkRepository dependency

    public GalleryService(GalleryRepository galleryRepo, StudentRepository studentRepo, ArtworkRepository artworkRepo) {
        this.galleryRepo = galleryRepo;
        this.studentRepo = studentRepo;
        this.artworkRepo = artworkRepo;
    }

    public GalleryDto getGalleryByStudentEmail(String email) {
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for email: " + email));

        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        return GalleryDto.fromEntity(gallery);
    }

    @Transactional
    public void updateGalleryStatus(String email, boolean isPublic) {
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for email: " + email));

        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        gallery.setPublic(isPublic);
        galleryRepo.save(gallery);
    }

    public List<GalleryDto> getPublicGalleries() {
        List<Gallery> publicGalleries = galleryRepo.findAllByIsPublic(true);

        return publicGalleries.stream()
                .map(GalleryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public GalleryDto getPublicGalleryByStudentId(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        if (!gallery.isPublic()) {
            throw new ResourceNotFoundException("Gallery is not public.");
        }

        return GalleryDto.fromEntity(gallery);
    }

    // --- NEW METHOD ---
    @Transactional
    public void setGalleryCoverPhoto(String email, Long artworkId) {
        // Find the student and their gallery
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for email: " + email));
        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        // Find the selected artwork
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with ID: " + artworkId));

        // Security Check: Ensure the artwork belongs to the user's gallery
        if (!artwork.getGallery().getId().equals(gallery.getId())) {
            throw new IllegalArgumentException("Artwork does not belong to this gallery.");
        }

        // Set the cover photo and save
        gallery.setCoverArtwork(artwork);
        galleryRepo.save(gallery);
    }
}