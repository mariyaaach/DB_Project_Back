package ru.itpark.mashacursah.infrastructure.repository.project;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.entity.project.Project;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.infrastructure.repository.project.dto.GetProjectDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<GetProjectDto> findById(Long projectId) {
        final String sql = """
                SELECT project_id, project_name, description, start_date, end_date, status, u.full_name AS manager_full_name, u.user_id AS manager_id
                FROM projects
                JOIN users u ON projects.manager_id = u.user_id
                WHERE project_id = :projectId
                """;

        try {
            GetProjectDto project = jdbcTemplate.queryForObject(sql, Map.of("projectId", projectId), new GetProjectDto.GetAllProjectsDtoMapper());
            return Optional.of(project);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<GetProjectDto> findAll() {
        final String sql = """
                SELECT project_id, project_name, description, start_date, end_date, status, u.full_name AS manager_full_name, u.user_id AS manager_id
                FROM projects 
                JOIN users u ON projects.manager_id = u.user_id
                """;
        return jdbcTemplate.query(sql, new GetProjectDto.GetAllProjectsDtoMapper());
    }

    public void save(Project project) {
        final String sql = """
                INSERT INTO projects (project_name, description, start_date, end_date, status, manager_id)
                VALUES (:projectName, :description, :startDate, :endDate, :status, :managerId)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("projectName", project.getProjectName())
                .addValue("description", project.getDescription())
                .addValue("startDate", project.getStartDate())
                .addValue("endDate", project.getEndDate())
                .addValue("status", project.getStatus())
                .addValue("managerId", project.getManagerId());

        jdbcTemplate.update(sql, params);
    }

    @Log
    public int update(Long projectId, Project project) {
        final String sql = """
                UPDATE projects
                SET project_name = COALESCE(project_name, :projectName),
                    description = COALESCE(description, :description),
                    start_date = COALESCE(start_date,:startDate),
                    end_date = COALESCE(end_date,:endDate),
                    status = COALESCE(status,:status),
                    manager_id = COALESCE(manager_id,:managerId)
                WHERE project_id = :projectId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("projectName", project.getProjectName())
                .addValue("description", project.getDescription())
                .addValue("startDate", project.getStartDate())
                .addValue("endDate", project.getEndDate())
                .addValue("status", project.getStatus())
                .addValue("managerId", project.getManagerId())
                .addValue("projectId", projectId);

        return jdbcTemplate.update(sql, params);
    }

    public int deleteById(Long projectId) {
        final String sql = "DELETE FROM projects WHERE project_id = :projectId";
        return jdbcTemplate.update(sql, Map.of("projectId", projectId));
    }
}