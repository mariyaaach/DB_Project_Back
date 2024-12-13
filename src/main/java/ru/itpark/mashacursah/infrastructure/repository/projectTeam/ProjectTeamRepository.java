package ru.itpark.mashacursah.infrastructure.repository.projectTeam;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.controllers.http.projectTeam.dto.AddTeamMemberDto;
import ru.itpark.mashacursah.controllers.http.projectTeam.dto.ProjectTeamMemberDto;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProjectTeamRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<ProjectTeamMemberDto> findTeamMembersByProjectId(Long projectId) {
        final String sql = """
            SELECT pt.project_id, pt.user_id, pt.role_in_project, u.full_name
            FROM project_team pt
            JOIN users u ON pt.user_id = u.user_id
            WHERE pt.project_id = :projectId
            """;
        return jdbcTemplate.query(sql, Map.of("projectId", projectId), (rs, rowNum) -> {
            ProjectTeamMemberDto dto = new ProjectTeamMemberDto();
            dto.setProjectId(rs.getLong("project_id"));
            dto.setUserId(rs.getLong("user_id"));
            dto.setFullName(rs.getString("full_name"));
            dto.setRoleInProject(rs.getString("role_in_project"));
            return dto;
        });
    }

    public void addTeamMember(Long projectId, AddTeamMemberDto dto) {
        final String sql = """
            INSERT INTO project_team (project_id, user_id, role_in_project)
            VALUES (:projectId, :userId, :roleInProject)
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("projectId", projectId)
            .addValue("userId", dto.getUserId())
            .addValue("roleInProject", dto.getRoleInProject());
        jdbcTemplate.update(sql, params);
    }

    public void updateTeamMemberRole(Long projectId, Long userId, String roleInProject) {
        final String sql = """
            UPDATE project_team
            SET role_in_project = :roleInProject
            WHERE project_id = :projectId AND user_id = :userId
            """;
        Map<String, Object> params = Map.of(
            "projectId", projectId,
            "userId", userId,
            "roleInProject", roleInProject
        );
        jdbcTemplate.update(sql, params);
    }

    public void removeTeamMember(Long projectId, Long userId) {
        final String sql = """
            DELETE FROM project_team
            WHERE project_id = :projectId AND user_id = :userId
            """;
        jdbcTemplate.update(sql, Map.of("projectId", projectId, "userId", userId));
    }
}