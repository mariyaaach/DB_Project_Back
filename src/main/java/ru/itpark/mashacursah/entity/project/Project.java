package ru.itpark.mashacursah.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("projects")
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    private Long projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long managerId;
}