package ru.itpark.mashacursah.service.task;

import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.task.Task;
import ru.itpark.mashacursah.infrastructure.repository.task.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public void createTask(Task task) {
        taskRepository.save(task);
    }

    public void updateTask(Task task) {
        taskRepository.update(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.delete(taskId);
    }
}