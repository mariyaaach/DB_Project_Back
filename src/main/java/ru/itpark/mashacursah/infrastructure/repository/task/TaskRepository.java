package ru.itpark.mashacursah.infrastructure.repository.task;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.entity.task.Task;

import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TaskRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    public Task findById(Long taskId) {
        String sql = "SELECT * FROM tasks WHERE task_id = :taskId";
        return jdbcTemplate.queryForObject(sql, Map.of("taskId", taskId), new TaskRowMapper());
    }

    public void save(Task task) {
        String sql = "INSERT INTO tasks (project_id, assigned_to, title, description, due_date, status) " +
                "VALUES (:projectId, :assignedTo, :title, :description, :dueDate, :status)";
        jdbcTemplate.update(sql, Map.of(
                "projectId", task.getProjectId(),
                "assignedTo", task.getAssignedTo(),
                "title", task.getTitle(),
                "description", task.getDescription(),
                "dueDate", task.getDueDate(),
                "status", task.getStatus()
        ));
    }

    public void update(Task task) {
        String sql = "UPDATE tasks SET project_id = :projectId, assigned_to = :assignedTo, title = :title, " +
                "description = :description, due_date = :dueDate, status = :status WHERE task_id = :taskId";
        jdbcTemplate.update(sql, Map.of(
                "taskId", task.getTaskId(),
                "projectId", task.getProjectId(),
                "assignedTo", task.getAssignedTo(),
                "title", task.getTitle(),
                "description", task.getDescription(),
                "dueDate", task.getDueDate(),
                "status", task.getStatus()
        ));
    }

    public void delete(Long taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = :taskId";
        jdbcTemplate.update(sql, Map.of("taskId", taskId));
    }
}