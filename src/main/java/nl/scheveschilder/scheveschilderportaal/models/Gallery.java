package nl.scheveschilder.scheveschilderportaal.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_public", nullable = false, columnDefinition = "boolean default true")
    private boolean isPublic = true;

    // Used to control the display order on the public hub page.
    // Defaults to a high number so new galleries appear at the end.
    @Column(name = "display_order")
    private Integer displayOrder = 1000;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Artwork> artworks = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "cover_artwork_id")
    private Artwork coverArtwork;


    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    // --- New Getter and Setter for displayOrder ---
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }

    public Artwork getCoverArtwork() {
        return coverArtwork;
    }

    public void setCoverArtwork(Artwork coverArtwork) {
        this.coverArtwork = coverArtwork;
    }
}