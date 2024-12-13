package ru.itpark.mashacursah.controllers.http.projectTeam;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.controllers.http.projectTeam.dto.AddTeamMemberDto;
import ru.itpark.mashacursah.controllers.http.projectTeam.dto.ProjectTeamMemberDto;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.projectTeam.ProjectTeamService;

import java.util.List;
import java.util.Map;

@RestController
@Log
@RequestMapping("/api/projects/{projectId}/team")
@RequiredArgsConstructor
public class ProjectTeamController {
    private final ProjectTeamService projectTeamService;

    @GetMapping
    public List<ProjectTeamMemberDto> getProjectTeam(@PathVariable Long projectId) {
        return projectTeamService.getProjectTeam(projectId);
    }

    @PostMapping
    public ResponseEntity<Void> addTeamMember(@PathVariable Long projectId, @RequestBody AddTeamMemberDto dto) {
        projectTeamService.addTeamMember(projectId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateTeamMemberRole(@PathVariable Long projectId, @PathVariable Long userId, @RequestBody Map<String, String> updates) {
        String roleInProject = updates.get("roleInProject");
        if (roleInProject == null) {
            return ResponseEntity.badRequest().body(null);
        }
        projectTeamService.updateTeamMemberRole(projectId, userId, roleInProject);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeTeamMember(@PathVariable Long projectId, @PathVariable Long userId) {
        projectTeamService.removeTeamMember(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}