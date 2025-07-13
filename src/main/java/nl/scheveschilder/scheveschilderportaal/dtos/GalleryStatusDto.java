package nl.scheveschilder.scheveschilderportaal.dtos;

import jakarta.validation.constraints.NotNull;

public class GalleryStatusDto {
    @NotNull(message = "Public status cannot be null")
    public Boolean isPublic;

    // Standard getter
    public Boolean getIsPublic() {
        return isPublic;
    }

    // Standard setter
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}
