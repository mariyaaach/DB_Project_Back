package ru.itpark.mashacursah.entity.project;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("project_budgets")
public class ProjectBudget {

    @Id
    private Long budgetId;

    private Long projectId;
    private BigDecimal allocatedAmount;
    private BigDecimal spentAmount;
}