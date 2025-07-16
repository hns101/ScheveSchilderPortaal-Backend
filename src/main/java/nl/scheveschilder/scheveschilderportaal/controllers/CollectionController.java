package nl.scheveschilder.scheveschilderportaal.controllers;

import jakarta.validation.Valid;
import nl.scheveschilder.scheveschilderportaal.dtos.ArtworkIdDto;
import nl.scheveschilder.scheveschilderportaal.dtos.CollectionDto;
import nl.scheveschilder.scheveschilderportaal.dtos.CollectionInputDto;
import nl.scheveschilder.scheveschilderportaal.dtos.GalleryOrderDto;
import nl.scheveschilder.scheveschilderportaal.service.CollectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/collections") // Base path for all admin collection endpoints
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping
    public ResponseEntity<List<CollectionDto>> getAllCollections() {
        return ResponseEntity.ok(collectionService.getAllCollections());
    }

    @PostMapping
    public ResponseEntity<CollectionDto> createCollection(@Valid @RequestBody CollectionInputDto dto) {
        CollectionDto newCollection = collectionService.createCollection(dto);
        return new ResponseEntity<>(newCollection, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/order")
    public ResponseEntity<Void> updateCollectionOrder(@RequestBody GalleryOrderDto orderDto) {
        collectionService.updateCollectionOrder(orderDto.getGalleryIds());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{collectionId}/artworks")
    public ResponseEntity<Void> addArtworkToCollection(@PathVariable Long collectionId, @Valid @RequestBody ArtworkIdDto artworkDto) {
        collectionService.addArtworkToCollection(collectionId, artworkDto.artworkId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{collectionId}/artworks/{artworkId}")
    public ResponseEntity<Void> removeArtworkFromCollection(@PathVariable Long collectionId, @PathVariable Long artworkId) {
        collectionService.removeArtworkFromCollection(collectionId, artworkId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{collectionId}/cover/{artworkId}")
    public ResponseEntity<Void> setCollectionCoverPhoto(@PathVariable Long collectionId, @PathVariable Long artworkId) {
        collectionService.setCollectionCoverPhoto(collectionId, artworkId);
        return ResponseEntity.noContent().build();
    }
}