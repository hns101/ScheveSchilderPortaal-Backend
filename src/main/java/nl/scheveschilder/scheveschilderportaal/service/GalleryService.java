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

    public GalleryDto getGalleryByStudent(Long studentId) {
        Gallery gallery = galleryRepo.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Gallery not found"));

        GalleryDto dto = new GalleryDto();
        dto.id = gallery.getId();
        dto.studentId = gallery.getStudent().getId();
        dto.artworks = gallery.getArtworks().stream().map(a -> {
            ArtworkDto adto = new ArtworkDto();
            adto.id = a.getId();
            adto.title = a.getTitle();
            adto.year = a.getYear();
            adto.photoUrl = a.getPhotoUrl();
            adto.artistId = studentId;
            return adto;
        }).collect(Collectors.toList());

        return dto;
    }
}
