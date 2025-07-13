package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.GalleryDto;
import nl.scheveschilder.scheveschilderportaal.exceptions.ResourceNotFoundException;
import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import nl.scheveschilder.scheveschilderportaal.models.Student;
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

    public GalleryService(GalleryRepository galleryRepo, StudentRepository studentRepo) {
        this.galleryRepo = galleryRepo;
        this.studentRepo = studentRepo;
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

    // --- NEW METHOD ---
    /**
     * Fetches all galleries that are marked as public.
     * @return A list of GalleryDto objects for all public galleries.
     */
    public List<GalleryDto> getPublicGalleries() {
        // Use the new repository method to find all galleries where isPublic is true
        List<Gallery> publicGalleries = galleryRepo.findAllByIsPublic(true);

        // Convert the list of Gallery entities to a list of GalleryDto objects
        return publicGalleries.stream()
                .map(GalleryDto::fromEntity)
                .collect(Collectors.toList());
    }
}