package ru.itpark.mashacursah.infrastructure.repository.project;

import org.springframework.jdbc.core.RowMapper;
import ru.itpark.mashacursah.entity.project.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectMapper implements RowMapper<Project> {
    @Override
    public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
        Project project = new Project();
        project.setProjectId(rs.getLong("project_id"));
        project.setProjectName(rs.getString("project_name"));
        project.setDescription(rs.getString("description"));
        project.setStartDate(rs.getDate("start_date").toLocalDate());
        project.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
        project.setStatus(rs.getString("status"));
        project.setManagerId(rs.getLong("manager_id"));
        return project;
    }
}