package ru.itpark.mashacursah.controllers.http.projectTeam.dto;

import lombok.Data;

@Data
public class AddTeamMemberDto {
    private Long userId;
    private String roleInProject;
}