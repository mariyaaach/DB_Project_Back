package ru.itpark.mashacursah.infrastructure.repository.publication;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.entity.publication.Publication;

import java.util.List;
import java.util.Map;

@Repository
public class PublicationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PublicationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Publication> findAll() {
        String sql = "SELECT * FROM publications";
        return jdbcTemplate.query(sql, new PublicationRowMapper());
    }

    public Publication findById(Long publicationId) {
        String sql = "SELECT * FROM publications WHERE publication_id = :publicationId";
        return jdbcTemplate.queryForObject(sql, Map.of("publicationId", publicationId), new PublicationRowMapper());
    }

    public void save(Publication publication) {
        String sql = "INSERT INTO publications (project_id, title, abstract, publication_date, link) " +
                     "VALUES (:projectId, :title, :abstractText, :publicationDate, :link)";
        jdbcTemplate.update(sql, Map.of(
                "projectId", publication.getProjectId(),
                "title", publication.getTitle(),
                "abstractText", publication.getAbstractText(),
                "publicationDate", publication.getPublicationDate(),
                "link", publication.getLink()
        ));
    }

    public void update(Publication publication) {
        String sql = "UPDATE publications SET project_id = :projectId, title = :title, abstract = :abstractText, " +
                     "publication_date = :publicationDate, link = :link WHERE publication_id = :publicationId";
        jdbcTemplate.update(sql, Map.of(
                "publicationId", publication.getPublicationId(),
                "projectId", publication.getProjectId(),
                "title", publication.getTitle(),
                "abstractText", publication.getAbstractText(),
                "publicationDate", publication.getPublicationDate(),
                "link", publication.getLink()
        ));
    }

    public void delete(Long publicationId) {
        String sql = "DELETE FROM publications WHERE publication_id = :publicationId";
        jdbcTemplate.update(sql, Map.of("publicationId", publicationId));
    }
}