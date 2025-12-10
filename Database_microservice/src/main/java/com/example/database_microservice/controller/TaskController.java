package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.TaskDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.database_microservice.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/db/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO TaskDTO) {
        return taskService.createTask(TaskDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO updatedTaskDTO) {
        return taskService.updateTask(id, updatedTaskDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}
