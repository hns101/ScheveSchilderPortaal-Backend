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

    public List<GalleryDto> getPublicGalleries() {
        List<Gallery> publicGalleries = galleryRepo.findAllByIsPublic(true);

        return publicGalleries.stream()
                .map(GalleryDto::fromEntity)
                .collect(Collectors.toList());
    }

    // --- NEW METHOD ---
    /**
     * Fetches a single gallery by student ID, but only if it's public.
     * @param studentId The ID of the student whose gallery is being requested.
     * @return A GalleryDto if the gallery is found and is public.
     * @throws ResourceNotFoundException if the gallery doesn't exist or is private.
     */
    public GalleryDto getPublicGalleryByStudentId(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        // Security check: only return the gallery if it's public
        if (!gallery.isPublic()) {
            throw new ResourceNotFoundException("Gallery is not public.");
        }

        return GalleryDto.fromEntity(gallery);
    }
}