package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Collection;

public class CollectionDto {
    public Long id;
    public String name;
    public Integer displayOrder;
    public Long coverArtworkId;

    public static CollectionDto fromEntity(Collection collection) {
        var dto = new CollectionDto();
        dto.id = collection.getId();
        dto.name = collection.getName();
        dto.displayOrder = collection.getDisplayOrder();
        if (collection.getCoverArtwork() != null) {
            dto.coverArtworkId = collection.getCoverArtwork().getId();
        }
        return dto;
    }
}