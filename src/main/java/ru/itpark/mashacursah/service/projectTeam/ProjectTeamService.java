package ru.itpark.mashacursah.service.projectTeam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.controllers.http.projectTeam.dto.AddTeamMemberDto;
import ru.itpark.mashacursah.controllers.http.projectTeam.dto.ProjectTeamMemberDto;
import ru.itpark.mashacursah.infrastructure.repository.projectTeam.ProjectTeamRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectTeamService {
    private final ProjectTeamRepository projectTeamRepository;

    public List<ProjectTeamMemberDto> getProjectTeam(Long projectId) {
        return projectTeamRepository.findTeamMembersByProjectId(projectId);
    }

    public void addTeamMember(Long projectId, AddTeamMemberDto dto) {
        projectTeamRepository.addTeamMember(projectId, dto);
    }

    public void updateTeamMemberRole(Long projectId, Long userId, String roleInProject) {
        projectTeamRepository.updateTeamMemberRole(projectId, userId, roleInProject);
    }

    public void removeTeamMember(Long projectId, Long userId) {
        projectTeamRepository.removeTeamMember(projectId, userId);
    }
}