package com.example.task_microservice.controller;

import com.example.task_microservice.DTOs.TaskDTO;
import com.example.task_microservice.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
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
        return taskService.updateTask(id, updatedTaskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }


    @GetMapping("/user/{id}")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long id) {
        return taskService.getTasksByUserId(id);

    }

}
