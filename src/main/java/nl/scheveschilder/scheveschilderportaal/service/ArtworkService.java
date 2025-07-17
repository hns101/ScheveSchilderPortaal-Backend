package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkDto;
import nl.scheveschilder.scheveschilderportaal.exceptions.ResourceNotFoundException;
import nl.scheveschilder.scheveschilderportaal.models.Artwork;
import nl.scheveschilder.scheveschilderportaal.models.Gallery;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.repositories.ArtworkRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.GalleryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepo;
    private final StudentRepository studentRepo;
    private final GalleryRepository galleryRepo;
    private final ArtworkPhotoService artworkPhotoService;

    public ArtworkService(ArtworkRepository artworkRepo, StudentRepository studentRepo, GalleryRepository galleryRepo, ArtworkPhotoService artworkPhotoService) {
        this.artworkRepo = artworkRepo;
        this.studentRepo = studentRepo;
        this.galleryRepo = galleryRepo;
        this.artworkPhotoService = artworkPhotoService;
    }

    public List<ArtworkDto> getArtworksByStudentEmail(String email) {
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student with email " + email + " not found"));
        Gallery gallery = galleryRepo.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found for student " + student.getId()));
        return artworkRepo.findByGallery(gallery).stream().map(ArtworkDto::fromEntity).toList();
    }

    public ArtworkDto addArtworkToStudent(String email, ArtworkDto dto) {
        Student student = studentRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student met email '" + email + "' niet gevonden."));
        return createAndSaveArtwork(student, dto);
    }

    // --- NEW: Admin method to add artwork by student ID ---
    public ArtworkDto adminAddArtworkToStudent(Long studentId, ArtworkDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student met ID '" + studentId + "' niet gevonden."));
        return createAndSaveArtwork(student, dto);
    }

    // Helper method to avoid code duplication
    private ArtworkDto createAndSaveArtwork(Student student, ArtworkDto dto) {
        Gallery gallery = galleryRepo.findByStudent(student).orElseGet(() -> {
            Gallery newGallery = new Gallery();
            newGallery.setStudent(student);
            return galleryRepo.save(newGallery);
        });
        Artwork artwork = new Artwork();
        artwork.setTitle(dto.title);
        artwork.setYear(dto.year);
        artwork.setPhotoUrl(dto.photoUrl);
        artwork.setArtist(student);
        artwork.setGallery(gallery);
        Artwork saved = artworkRepo.save(artwork);
        return ArtworkDto.fromEntity(saved);
    }

    public void removeArtwork(String email, Long artworkId) {
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        String photoFile = artwork.getPhotoUrl();
        if (photoFile != null && !photoFile.isEmpty()) {
            artworkPhotoService.deletePhoto(photoFile);
        }
        artworkRepo.delete(artwork);
    }

    @Transactional
    public void adminDeleteArtwork(Long artworkId) {
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with ID: " + artworkId));
        String photoFile = artwork.getPhotoUrl();
        if (photoFile != null && !photoFile.isEmpty()) {
            artworkPhotoService.deletePhoto(photoFile);
        }
        artworkRepo.delete(artwork);
    }

    public void assignPhotoToArtwork(Long artworkId, String photoFileName) {
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork niet gevonden"));
        artwork.setPhotoUrl(photoFileName);
        artworkRepo.save(artwork);
    }

    public String getArtworkPhotoFileName(Long artworkId) {
        return artworkRepo.findById(artworkId).map(Artwork::getPhotoUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Foto niet gevonden"));
    }

    public String getPublicArtworkPhotoFileName(Long artworkId) {
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with ID: " + artworkId));
        if (artwork.getGallery() == null || !artwork.getGallery().isPublic()) {
            throw new ResourceNotFoundException("Artwork is not in a public gallery.");
        }
        return artwork.getPhotoUrl();
    }
}
