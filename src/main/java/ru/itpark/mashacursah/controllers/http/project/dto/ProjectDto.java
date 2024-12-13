package ru.itpark.mashacursah.controllers.http.project.dto;

import ru.itpark.mashacursah.entity.project.Project;

import java.time.LocalDate;

/**
 * DTO for {@link Project}
 */
public record ProjectDto(Long projectId, String projectName, String description, LocalDate startDate, LocalDate endDate,
                         String status) {
}