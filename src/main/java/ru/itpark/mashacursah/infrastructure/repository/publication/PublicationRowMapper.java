package ru.itpark.mashacursah.infrastructure.repository.publication;

import org.springframework.jdbc.core.RowMapper;
import ru.itpark.mashacursah.entity.publication.Publication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PublicationRowMapper implements RowMapper<Publication> {

    @Override
    public Publication mapRow(ResultSet rs, int rowNum) throws SQLException {
        Publication publication = new Publication();
        
        publication.setPublicationId(rs.getLong("publication_id"));
        publication.setProjectId(rs.getLong("project_id"));
        publication.setTitle(rs.getString("title"));
        publication.setAbstractText(rs.getString("abstract"));
        publication.setPublicationDate(rs.getObject("publication_date", LocalDate.class));
        publication.setLink(rs.getString("link"));
        
        return publication;
    }
}