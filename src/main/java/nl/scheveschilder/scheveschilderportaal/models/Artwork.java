package nl.scheveschilder.scheveschilderportaal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String year;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student artist;

    // --- NEW FIELD ---
    // This completes the many-to-many relationship.
    // 'mappedBy = "artworks"' indicates that the Collection entity manages the relationship table.
    @ManyToMany(mappedBy = "artworks", fetch = FetchType.LAZY)
    @JsonIgnore // Prevents infinite loops when serializing to JSON
    private Set<Collection> collections = new HashSet<>();

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public Student getArtist() {
        return artist;
    }

    public void setArtist(Student artist) {
        this.artist = artist;
    }

    // --- New Getter and Setter for collections ---
    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }
}