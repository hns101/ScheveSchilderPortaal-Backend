package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.GalleryDto;
import nl.scheveschilder.scheveschilderportaal.exceptions.ResourceNotFoundException;
import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.repositories.GalleryRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GalleryService {

    private final GalleryRepository galleryRepo;
    private final StudentRepository studentRepo;

    public GalleryService(GalleryRepository galleryRepo, StudentRepository studentRepo) {
        this.galleryRepo = galleryRepo;
        this.studentRepo = studentRepo;
    }

    public GalleryDto getGalleryByStudentEmail(String email) {
        // A more direct way to find the student
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for email: " + email));

        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        return GalleryDto.fromEntity(gallery);
    }

    // --- NEW METHOD ---
    @Transactional
    public void updateGalleryStatus(String email, boolean isPublic) {
        // Find the student associated with the email
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for email: " + email));

        // Find their gallery
        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student: " + student.getId()));

        // Update the status and save
        gallery.setPublic(isPublic);
        galleryRepo.save(gallery);
    }
}
