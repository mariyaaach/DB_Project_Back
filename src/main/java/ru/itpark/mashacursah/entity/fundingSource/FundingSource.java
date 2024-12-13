package ru.itpark.mashacursah.entity.fundingSource;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("funding_sources")
public class FundingSource {

    @Id
    private Long fundingSourceId;

    private String name;
    private String description;
}