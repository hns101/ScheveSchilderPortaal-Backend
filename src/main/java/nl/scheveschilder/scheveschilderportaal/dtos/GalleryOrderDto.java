package nl.scheveschilder.scheveschilderportaal.dtos;

import java.util.List;

public class GalleryOrderDto {

    private List<Long> galleryIds;

    public List<Long> getGalleryIds() {
        return galleryIds;
    }

    public void setGalleryIds(List<Long> galleryIds) {
        this.galleryIds = galleryIds;
    }
}
