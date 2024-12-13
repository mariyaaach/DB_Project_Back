package ru.itpark.mashacursah.controllers.http.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.controllers.http.project.dto.ProjectMapper;
import ru.itpark.mashacursah.entity.project.Project;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.infrastructure.repository.project.dto.GetProjectDto;
import ru.itpark.mashacursah.service.project.ProjectService;

import java.util.List;

@RestController
@Log
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping("/{id}")
    public ResponseEntity<GetProjectDto> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<GetProjectDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    public ResponseEntity createProject(@RequestBody Project project) {
        projectService.createProject(project);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProject(@PathVariable Long id, @RequestBody Project project) {
        if (projectService.updateProject(id, project)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProject(@PathVariable Long id) {
        if (projectService.deleteProject(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}