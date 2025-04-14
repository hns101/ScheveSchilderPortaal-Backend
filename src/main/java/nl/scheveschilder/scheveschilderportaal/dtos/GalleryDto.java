package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Gallery;

import java.util.List;
import java.util.stream.Collectors;

public class GalleryDto {
    public Long id;
    public Long studentId;
    public List<ArtworkDto> artworks;

    public static GalleryDto fromEntity(Gallery gallery) {
        GalleryDto dto = new GalleryDto();
        dto.id = gallery.getId();
        dto.studentId = gallery.getStudent().getId();
        dto.artworks = gallery.getArtworks().stream()
                .map(ArtworkDto::fromEntity)
                .collect(Collectors.toList());
        return dto;
    }
}