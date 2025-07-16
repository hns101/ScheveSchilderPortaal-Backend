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
    private final ArtworkRepository artworkRepo;

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
        // Use the new repository method to get sorted galleries
        List<Gallery> publicGalleries = galleryRepo.findAllByIsPublicTrueOrderByDisplayOrderAsc();

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

    @Transactional
    public void setGalleryCoverPhoto(String email, Long artworkId) {
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for email: " + email));
        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with ID: " + artworkId));

        if (!artwork.getGallery().getId().equals(gallery.getId())) {
            throw new IllegalArgumentException("Artwork does not belong to this gallery.");
        }

        gallery.setCoverArtwork(artwork);
        galleryRepo.save(gallery);
    }

    // --- NEW METHOD for Admin ---
    @Transactional
    public void updateGalleryOrder(List<Long> galleryIds) {
        for (int i = 0; i < galleryIds.size(); i++) {
            Long galleryId = galleryIds.get(i);
            int displayOrder = i; // The order is determined by the position in the list

            galleryRepo.findById(galleryId).ifPresent(gallery -> {
                gallery.setDisplayOrder(displayOrder);
                galleryRepo.save(gallery);
            });
        }
    }
}