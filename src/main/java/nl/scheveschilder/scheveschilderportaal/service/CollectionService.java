package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.CollectionDetailDto;
import nl.scheveschilder.scheveschilderportaal.dtos.CollectionDto;
import nl.scheveschilder.scheveschilderportaal.dtos.CollectionInputDto;
import nl.scheveschilder.scheveschilderportaal.exceptions.ResourceNotFoundException;
import nl.scheveschilder.scheveschilderportaal.models.Artwork;
import nl.scheveschilder.scheveschilderportaal.models.Collection;
import nl.scheveschilder.scheveschilderportaal.repositories.ArtworkRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.CollectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepo;
    private final ArtworkRepository artworkRepo;

    public CollectionService(CollectionRepository collectionRepo, ArtworkRepository artworkRepo) {
        this.collectionRepo = collectionRepo;
        this.artworkRepo = artworkRepo;
    }

    public CollectionDetailDto getCollectionById(Long id) {
        Collection collection = collectionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + id));
        return CollectionDetailDto.fromEntity(collection);
    }

    public List<CollectionDto> getAllCollections() {
        return collectionRepo.findAllByOrderByDisplayOrderAsc().stream()
                .map(CollectionDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CollectionDto createCollection(CollectionInputDto dto) {
        Collection newCollection = new Collection();
        newCollection.setName(dto.name);
        Collection savedCollection = collectionRepo.save(newCollection);
        return CollectionDto.fromEntity(savedCollection);
    }

    @Transactional
    public void deleteCollection(Long id) {
        if (!collectionRepo.existsById(id)) {
            throw new ResourceNotFoundException("Collection not found with ID: " + id);
        }
        collectionRepo.deleteById(id);
    }

    @Transactional
    public void updateCollectionOrder(List<Long> collectionIds) {
        for (int i = 0; i < collectionIds.size(); i++) {
            Long collectionId = collectionIds.get(i);
            int displayOrder = i;
            collectionRepo.findById(collectionId).ifPresent(collection -> {
                collection.setDisplayOrder(displayOrder);
                collectionRepo.save(collection);
            });
        }
    }

    @Transactional
    public void addArtworkToCollection(Long collectionId, Long artworkId) {
        Collection collection = collectionRepo.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + collectionId));
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with ID: " + artworkId));
        collection.getArtworks().add(artwork);
        collectionRepo.save(collection);
    }

    @Transactional
    public void removeArtworkFromCollection(Long collectionId, Long artworkId) {
        Collection collection = collectionRepo.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + collectionId));
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with ID: " + artworkId));
        collection.getArtworks().remove(artwork);
        collectionRepo.save(collection);
    }

    @Transactional
    public void setCollectionCoverPhoto(Long collectionId, Long artworkId) {
        Collection collection = collectionRepo.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + collectionId));
        Artwork artwork = artworkRepo.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with ID: " + artworkId));

        if (!collection.getArtworks().contains(artwork)) {
            throw new IllegalArgumentException("Artwork must be in the collection to be set as cover.");
        }

        collection.setCoverArtwork(artwork);
        collectionRepo.save(collection);
    }
}