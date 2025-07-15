package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Artwork;
import nl.scheveschilder.scheveschilderportaal.models.Gallery;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GalleryDto {
    public Long id;
    public PublicStudentDto student;
    public List<ArtworkDto> artworks;
    public boolean isPublic;

    public static GalleryDto fromEntity(Gallery gallery) {
        GalleryDto dto = new GalleryDto();
        dto.id = gallery.getId();
        dto.student = PublicStudentDto.fromEntity(gallery.getStudent());
        dto.isPublic = gallery.isPublic();

        // -- Sort artworks by year descending ---
        if (gallery.getArtworks() != null) {
            dto.artworks = gallery.getArtworks().stream()
                    .sorted(Comparator.comparing(Artwork::getYear, Comparator.nullsLast(Comparator.reverseOrder())))
                    .map(ArtworkDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return dto;
    }
}