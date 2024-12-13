package ru.itpark.mashacursah.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.project.Project;
import ru.itpark.mashacursah.infrastructure.repository.project.ProjectRepository;
import ru.itpark.mashacursah.infrastructure.repository.project.dto.GetProjectDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Optional<GetProjectDto> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public List<GetProjectDto> getAllProjects() {
        return projectRepository.findAll();
    }

    public void createProject(Project project) {
        projectRepository.save(project);
    }

    public boolean updateProject(Long projectId, Project project) {
        return projectRepository.update(projectId, project) > 0;
    }

    public boolean deleteProject(Long projectId) {
        return projectRepository.deleteById(projectId) > 0;
    }
}