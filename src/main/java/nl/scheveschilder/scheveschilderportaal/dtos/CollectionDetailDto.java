package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Artwork;
import nl.scheveschilder.scheveschilderportaal.models.Collection;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionDetailDto {
    public Long id;
    public String name;
    public Integer displayOrder;
    public Long coverArtworkId;
    public List<ArtworkDto> artworks; // The full list of artworks

    public static CollectionDetailDto fromEntity(Collection collection) {
        var dto = new CollectionDetailDto();
        dto.id = collection.getId();
        dto.name = collection.getName();
        dto.displayOrder = collection.getDisplayOrder();
        if (collection.getCoverArtwork() != null) {
            dto.coverArtworkId = collection.getCoverArtwork().getId();
        }

        if (collection.getArtworks() != null) {
            // Here you could add custom sorting logic for artworks within a collection if needed
            dto.artworks = collection.getArtworks().stream()
                    .map(ArtworkDto::fromEntity)
                    .collect(Collectors.toList());
        }
        return dto;
    }
}