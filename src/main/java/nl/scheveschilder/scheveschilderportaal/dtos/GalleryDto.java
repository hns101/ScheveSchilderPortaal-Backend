package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Gallery;

import java.util.List;
import java.util.stream.Collectors;

public class GalleryDto {
    public Long id;
    // public Long studentId; // We are replacing this...
    public PublicStudentDto student; // ...with this safe DTO.
    public List<ArtworkDto> artworks;
    public boolean isPublic;

    public static GalleryDto fromEntity(Gallery gallery) {
        GalleryDto dto = new GalleryDto();
        dto.id = gallery.getId();
        dto.student = PublicStudentDto.fromEntity(gallery.getStudent()); // Use the new DTO
        dto.isPublic = gallery.isPublic();
        dto.artworks = gallery.getArtworks().stream()
                .map(ArtworkDto::fromEntity)
                .collect(Collectors.toList());
        return dto;
    }
}
