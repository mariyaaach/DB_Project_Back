package ru.itpark.mashacursah.infrastructure.repository.task;

import org.springframework.jdbc.core.RowMapper;
import ru.itpark.mashacursah.entity.task.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        
        task.setTaskId(rs.getLong("task_id"));
        task.setProjectId(rs.getLong("project_id"));
        task.setAssignedTo(rs.getLong("assigned_to"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setDueDate(rs.getDate("due_date").toLocalDate());
        task.setStatus(rs.getString("status"));
        
        return task;
    }
}