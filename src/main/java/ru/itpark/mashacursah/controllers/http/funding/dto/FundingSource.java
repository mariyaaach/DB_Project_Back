package ru.itpark.mashacursah.controllers.http.funding.dto;

import lombok.Data;

@Data
public class FundingSource {
    private Long fundingSourceId;
    private String name;
    private String description;
}