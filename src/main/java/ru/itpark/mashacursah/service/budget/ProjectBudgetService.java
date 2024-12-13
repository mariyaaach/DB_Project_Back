package ru.itpark.mashacursah.service.budget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.controllers.http.budget.dto.ProjectBudgetDto;
import ru.itpark.mashacursah.infrastructure.repository.budget.ProjectBudgetRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectBudgetService {
    private final ProjectBudgetRepository projectBudgetRepository;

    public Optional<ProjectBudgetDto> getProjectBudget(Long projectId) {
        return projectBudgetRepository.findByProjectId(projectId);
    }

    public void createProjectBudget(Long projectId, ProjectBudgetDto dto) {
        projectBudgetRepository.save(projectId, dto);
    }

    public boolean updateProjectBudget(Long projectId, ProjectBudgetDto dto) {
        if (getProjectBudget(projectId).isEmpty()) {
            createProjectBudget(projectId, dto);
            return true;
        }
        return projectBudgetRepository.update(projectId, dto) > 0;
    }
}