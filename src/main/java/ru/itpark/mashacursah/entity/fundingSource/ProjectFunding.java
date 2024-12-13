package ru.itpark.mashacursah.entity.fundingSource;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("project_funding")
public class ProjectFunding {

    private Long projectId;
    private Long fundingSourceId;
    private BigDecimal amount;
}