package com.example.database_microservice.service;

import org.springframework.stereotype.Service;
import com.example.database_microservice.DTOs.TaskDTO;
import com.example.database_microservice.model.Task;
import com.example.database_microservice.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // --- Mapping ---
    private TaskDTO toDTO(Task task) {
        return new TaskDTO(task);
    }

    // --- CRUD ---
    public List<TaskDTO> getAllTasks() {
//        var tasks = taskRepository.findAll();
        return taskRepository.findAll().stream().map(this::toDTO).toList();
//        return new ArrayList<>();
    }

    public Optional<TaskDTO> getTaskById(Long id) {
        return taskRepository.findById(id).map(this::toDTO);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task(taskDTO);
        return toDTO(taskRepository.save(task));
    }

    public Optional<TaskDTO> updateTask(Long id, TaskDTO updatedTaskDTO) {
        Task updatedTask = new Task(updatedTaskDTO);
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setDateToExecute(updatedTask.getDateToExecute());
            task.setDone(updatedTask.isDone());
            return toDTO(taskRepository.save(task));
        });
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
