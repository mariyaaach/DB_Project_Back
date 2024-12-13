package ru.itpark.mashacursah.service.publication;

import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.publication.Publication;
import ru.itpark.mashacursah.infrastructure.repository.publication.PublicationRepository;

import java.util.List;

@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;

    public PublicationService(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    public Publication getPublicationById(Long publicationId) {
        return publicationRepository.findById(publicationId);
    }

    public void createPublication(Publication publication) {
        publicationRepository.save(publication);
    }

    public void updatePublication(Publication publication) {
        publicationRepository.update(publication);
    }

    public void deletePublication(Long publicationId) {
        publicationRepository.delete(publicationId);
    }
}