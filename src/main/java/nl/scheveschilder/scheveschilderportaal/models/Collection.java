package nl.scheveschilder.scheveschilderportaal.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "display_order")
    private Integer displayOrder = 1000; // Default to appear at the end

    @OneToOne
    @JoinColumn(name = "cover_artwork_id")
    private Artwork coverArtwork;

    // A collection can have many artworks, and an artwork can be in many collections.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "collection_artworks",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "artwork_id")
    )
    private Set<Artwork> artworks = new HashSet<>();

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Artwork getCoverArtwork() {
        return coverArtwork;
    }

    public void setCoverArtwork(Artwork coverArtwork) {
        this.coverArtwork = coverArtwork;
    }

    public Set<Artwork> getArtworks() {
        return artworks;
    }

    public void setArtworks(Set<Artwork> artworks) {
        this.artworks = artworks;
    }
}