package ru.itpark.mashacursah.entity.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("tasks")
public class Task {

    @Id
    private Long taskId;

    private Long projectId;
    private Long assignedTo;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;
}