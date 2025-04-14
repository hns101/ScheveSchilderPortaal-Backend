package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkDto;
import nl.scheveschilder.scheveschilderportaal.dtos.GalleryDto;
import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.repositories.GalleryRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import org.springframework.stereotype.Service;

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
        Student student = studentRepo.findAll().stream()
                .filter(s -> s.getUser() != null && s.getUser().getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student niet gevonden"));

        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new IllegalArgumentException("Gallery niet gevonden"));

        return GalleryDto.fromEntity(gallery);
    }


}
