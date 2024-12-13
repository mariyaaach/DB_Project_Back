package ru.itpark.mashacursah.controllers.http.budget.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectBudgetDto {
    private BigDecimal allocatedAmount;
    private BigDecimal spentAmount;
}