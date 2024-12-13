package ru.itpark.mashacursah.infrastructure.repository.budget;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.controllers.http.budget.dto.ProjectBudgetDto;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectBudgetRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<ProjectBudgetDto> findByProjectId(Long projectId) {
        final String sql = """
            SELECT allocated_amount, spent_amount
            FROM project_budgets
            WHERE project_id = :projectId
            """;
        try {
            ProjectBudgetDto dto = jdbcTemplate.queryForObject(sql, Map.of("projectId", projectId), (rs, rowNum) -> {
                ProjectBudgetDto budgetDto = new ProjectBudgetDto();
                budgetDto.setAllocatedAmount(rs.getBigDecimal("allocated_amount"));
                budgetDto.setSpentAmount(rs.getBigDecimal("spent_amount"));
                return budgetDto;
            });
            return Optional.of(dto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(Long projectId, ProjectBudgetDto dto) {
        final String sql = """
            INSERT INTO project_budgets (project_id, allocated_amount, spent_amount)
            VALUES (:projectId, :allocatedAmount, :spentAmount)
            """;
        Map<String, Object> params = Map.of(
            "projectId", projectId,
            "allocatedAmount", dto.getAllocatedAmount(),
            "spentAmount", dto.getSpentAmount()
        );
        jdbcTemplate.update(sql, params);
    }

    public int update(Long projectId, ProjectBudgetDto dto) {
        final String sql = """
            UPDATE project_budgets
            SET allocated_amount = :allocatedAmount, spent_amount = :spentAmount
            WHERE project_id = :projectId
            """;
        Map<String, Object> params = Map.of(
            "projectId", projectId,
            "allocatedAmount", dto.getAllocatedAmount(),
            "spentAmount", dto.getSpentAmount()
        );
        return jdbcTemplate.update(sql, params);
    }
}