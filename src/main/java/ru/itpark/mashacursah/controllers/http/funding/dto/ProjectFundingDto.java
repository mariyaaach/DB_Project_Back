package ru.itpark.mashacursah.controllers.http.funding.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectFundingDto {
    private Long fundingSourceId;
    private String fundingSourceName;
    private BigDecimal amount;
}