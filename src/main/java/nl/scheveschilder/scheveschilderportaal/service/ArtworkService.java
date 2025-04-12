package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkDto;
import nl.scheveschilder.scheveschilderportaal.models.Artwork;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.repositories.ArtworkRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepo;
    private final StudentRepository studentRepo;

    public ArtworkService(ArtworkRepository artworkRepo, StudentRepository studentRepo) {
        this.artworkRepo = artworkRepo;
        this.studentRepo = studentRepo;
    }

    public ArtworkDto addArtwork(ArtworkDto dto) {
        Student student = studentRepo.findById(dto.artistId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Artwork artwork = new Artwork();
        artwork.setTitle(dto.title);
        artwork.setYear(dto.year);
        artwork.setPhotoUrl(dto.photoUrl);
        artwork.setArtist(student);

        Artwork saved = artworkRepo.save(artwork);
        dto.id = saved.getId();
        return dto;
    }

    public List<ArtworkDto> getArtworksByStudent(Long studentId) {
        return artworkRepo.findByArtistId(studentId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ArtworkDto toDto(Artwork a) {
        ArtworkDto dto = new ArtworkDto();
        dto.id = a.getId();
        dto.title = a.getTitle();
        dto.year = a.getYear();
        dto.photoUrl = a.getPhotoUrl();
        dto.artistId = a.getArtist().getId();
        return dto;
    }
}
