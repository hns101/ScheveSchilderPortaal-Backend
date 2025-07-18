package nl.scheveschilder.scheveschilderportaal.dtos;

import nl.scheveschilder.scheveschilderportaal.models.Artwork;

public class ArtworkDto {
    public Long id;
    public String title;
    public String year;
    public String photoUrl;
    public Long galleryId;
    public PublicStudentDto artist; // Changed from String artistName to the safe DTO

    public static ArtworkDto fromEntity(Artwork artwork) {
        ArtworkDto dto = new ArtworkDto();
        dto.id = artwork.getId();
        dto.title = artwork.getTitle();
        dto.year = artwork.getYear();
        dto.photoUrl = artwork.getPhotoUrl();
        dto.galleryId = artwork.getGallery() != null ? artwork.getGallery().getId() : null;

        // Use the safe PublicStudentDto to include the artist's ID and formatted name
        dto.artist = PublicStudentDto.fromEntity(artwork.getArtist());

        return dto;
    }

    public Artwork toEntity() {
        Artwork artwork = new Artwork();
        artwork.setId(this.id);
        artwork.setTitle(this.title);
        artwork.setYear(this.year);
        artwork.setPhotoUrl(this.photoUrl);
        // gallery and artist should be set in the service
        return artwork;
    }
}
