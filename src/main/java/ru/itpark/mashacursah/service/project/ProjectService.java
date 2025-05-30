package ru.itpark.mashacursah.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.project.Project;
import ru.itpark.mashacursah.infrastructure.configuration.redis.publisher.NotificationPublisher;
import ru.itpark.mashacursah.infrastructure.configuration.redis.publisher.dto.NotificationEvent;
import ru.itpark.mashacursah.infrastructure.repository.project.ProjectRepository;
import ru.itpark.mashacursah.infrastructure.repository.project.dto.GetProjectDto;

import java.util.List;
import java.util.Optional;

import ru.itpark.mashacursah.service.redis.RedisService;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final RedisService redisService;
    private final NotificationPublisher notificationPublisher;

    private static final String PROJECTS_CACHE_KEY = "projects:all";

    public List<GetProjectDto> getAllProjects() {
        return redisService.getCachedList(
                PROJECTS_CACHE_KEY,
                GetProjectDto.class,
                projectRepository::findAll
        );
    }

    public Optional<GetProjectDto> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public void createProject(Project project) {
        projectRepository.save(project);
        redisService.clearCache(PROJECTS_CACHE_KEY);

        // Публикуем событие
        NotificationEvent event = new NotificationEvent();
        event.setType("Маша молодец, Маша создала проект!!");
        event.setMessage("Созданный проект: " + project.getProjectName());
        notificationPublisher.publishEvent("notifications", event);
    }

    public boolean updateProject(Long projectId, Project project) {
        boolean updated = projectRepository.update(projectId, project) > 0;
        if (updated) {
            redisService.clearCache(PROJECTS_CACHE_KEY);
        }
        return updated;
    }

    public boolean deleteProject(Long projectId) {
        boolean deleted = projectRepository.deleteById(projectId) > 0;
        if (deleted) {
            redisService.clearCache(PROJECTS_CACHE_KEY);
        }
        return deleted;
    }
}