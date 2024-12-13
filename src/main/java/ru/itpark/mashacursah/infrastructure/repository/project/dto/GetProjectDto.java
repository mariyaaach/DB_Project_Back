package ru.itpark.mashacursah.infrastructure.repository.project.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public record GetProjectDto(
        long projectId,
        String projectName,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String managerFullName,
        Long managerId
) {
    public static class GetAllProjectsDtoMapper implements RowMapper<GetProjectDto> {
        @Override
        public GetProjectDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GetProjectDto(
                    rs.getLong("project_id"),
                    rs.getString("project_name"),
                    rs.getString("description"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    rs.getString("status"),
                    rs.getString("manager_full_name"),
                    rs.getLong("manager_id")
            );
        }
    }
}
