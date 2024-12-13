package ru.itpark.mashacursah.entity.publication;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("publications")
public class Publication {

    @Id
    private Long publicationId;

    private Long projectId;
    private String title;
    private String abstractText;
    private LocalDate publicationDate;
    private String link;
}