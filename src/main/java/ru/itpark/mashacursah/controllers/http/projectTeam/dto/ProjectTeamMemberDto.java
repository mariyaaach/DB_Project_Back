package ru.itpark.mashacursah.controllers.http.projectTeam.dto;

import lombok.Data;

@Data
public class ProjectTeamMemberDto {
    private Long projectId;
    private Long userId;
    private String fullName;
    private String roleInProject;
}

