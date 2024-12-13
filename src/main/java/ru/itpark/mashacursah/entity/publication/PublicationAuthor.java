package ru.itpark.mashacursah.entity.publication;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("publication_authors")
public class PublicationAuthor {

    private Long publicationId;
    private Long userId;
}