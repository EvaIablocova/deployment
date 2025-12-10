package com.example.task_microservice.service;

import com.example.task_microservice.DTOs.TaskDTO;
import com.example.task_microservice.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.getAllTasks();
    }

   public Optional<TaskDTO> getTaskById(Long id) {
       return Optional.ofNullable(taskRepository.getTaskById(id))
                      .map(response -> response.getBody());
   }

    public TaskDTO createTask(TaskDTO taskDTO) {
        return taskRepository.createTask(taskDTO).getBody();
    }

    public ResponseEntity<TaskDTO> updateTask(Long id, TaskDTO updatedTaskDTO) {
            return taskRepository.updateTask(id, updatedTaskDTO);
    }

    public ResponseEntity<Void> deleteTask(Long id) {
        return taskRepository.deleteTask(id);
    }


    public List<TaskDTO> getTasksByUserId(Long id) {
        List<TaskDTO> tasks = taskRepository.getAllTasks();
        return tasks.stream()
                .filter(task -> task.getUserId().equals(id))
                .toList();

    }


}
