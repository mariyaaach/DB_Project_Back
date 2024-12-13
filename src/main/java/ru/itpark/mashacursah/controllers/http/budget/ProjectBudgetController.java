package ru.itpark.mashacursah.controllers.http.budget;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.controllers.http.budget.dto.ProjectBudgetDto;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.budget.ProjectBudgetService;

@RestController
@Log
@RequiredArgsConstructor
public class ProjectBudgetController {
    private final ProjectBudgetService projectBudgetService;

    @GetMapping("/api/projects/{projectId}/budget")
    public ResponseEntity<ProjectBudgetDto> getProjectBudget(@PathVariable Long projectId) {
        return projectBudgetService.getProjectBudget(projectId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/projects/{projectId}/budget")
    public ResponseEntity<Void> createProjectBudget(@PathVariable Long projectId, @RequestBody ProjectBudgetDto dto) {
        projectBudgetService.createProjectBudget(projectId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/api/projects/{projectId}/budget")
    public ResponseEntity<Void> updateProjectBudget(@PathVariable Long projectId, @RequestBody ProjectBudgetDto dto) {
        if (projectBudgetService.updateProjectBudget(projectId, dto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}