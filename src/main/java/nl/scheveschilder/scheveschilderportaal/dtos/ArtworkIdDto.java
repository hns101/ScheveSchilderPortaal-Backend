package nl.scheveschilder.scheveschilderportaal.dtos;

import jakarta.validation.constraints.NotNull;

public class ArtworkIdDto {
    @NotNull(message = "Artwork ID is required")
    public Long artworkId;
}