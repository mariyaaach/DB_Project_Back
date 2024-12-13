package ru.itpark.mashacursah.controllers.http.publication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.entity.publication.Publication;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.publication.PublicationService;

import java.util.List;

@RestController
@Log
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping
    public List<Publication> getAllPublications() {
        return publicationService.getAllPublications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id) {
        Publication publication = publicationService.getPublicationById(id);
        return publication != null ? ResponseEntity.ok(publication) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createPublication(@RequestBody Publication publication) {
        publicationService.createPublication(publication);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePublication(@PathVariable Long id, @RequestBody Publication publication) {
        publication.setPublicationId(id);
        publicationService.updatePublication(publication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }
}