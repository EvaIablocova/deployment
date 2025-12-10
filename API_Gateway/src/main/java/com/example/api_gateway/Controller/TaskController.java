package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.TaskDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/tasks")
public class TaskController {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://taskmicroservice:9010/api/tasks";

    public TaskController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    // --- CRUD ---

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        try {
            ResponseEntity<TaskDTO[]> response = restTemplate.getForEntity(externalBase, TaskDTO[].class);
            TaskDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new TaskDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            ResponseEntity<TaskDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, TaskDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        try {
            ResponseEntity<TaskDTO> response =
                    restTemplate.postForEntity(externalBase, taskDTO, TaskDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO updatedTaskDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedTaskDTO);
            return ResponseEntity.ok(updatedTaskDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
