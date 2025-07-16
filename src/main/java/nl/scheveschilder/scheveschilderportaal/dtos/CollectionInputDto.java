package nl.scheveschilder.scheveschilderportaal.dtos;

import jakarta.validation.constraints.NotBlank;

public class CollectionInputDto {
    @NotBlank(message = "Collection name is required")
    public String name;
}