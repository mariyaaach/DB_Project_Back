package ru.itpark.mashacursah.entity.project;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("project_team")
public class ProjectTeam {

    private Long projectId;
    private Long userId;
    private String roleInProject;
}