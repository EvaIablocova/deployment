package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.TaskDTO;
import com.example.api_gateway.DTOs.UserDTO;
import com.example.api_gateway.DTOs.TaskDTOfrom;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@RestController
@RequestMapping("/api_gateway/tasks")
public class TaskController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    private final UserController userController;

    public TaskController(RestTemplateBuilder builder, UserController userController, ServiceUrlsConfig serviceUrls) {
        this.userController = userController;
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getTaskServiceUrl() + "/api/tasks";
    }

    private String getUsernameById(Long id) {
        return userController.getUsernameById(id).orElse("Unknown User");
    }

    // --- CRUD ---

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        try {
            ResponseEntity<TaskDTOfrom[]> response = restTemplate.getForEntity(externalBase, TaskDTOfrom[].class);
            TaskDTOfrom[] body = response.getBody();

            if (body == null) {
                return List.of();
            }

            List<TaskDTO> newBody = new ArrayList<>();
            for (TaskDTOfrom taskFrom : body) {
                String username = getUsernameById(taskFrom.getUserId());
                newBody.add(new TaskDTO(taskFrom, username));
            }

            return newBody;
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            ResponseEntity<TaskDTOfrom> response =
                    restTemplate.getForEntity(externalBase + "/" + id, TaskDTOfrom.class);

            return Optional.ofNullable(response.getBody())
                    .map(body -> {
                        String username = getUsernameById(body.getUserId());
                        return new TaskDTO(body, username);
                    })
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        try {
            TaskDTOfrom taskDTOfrom = new TaskDTOfrom(taskDTO);
            ResponseEntity<TaskDTOfrom> response =
                    restTemplate.postForEntity(externalBase, taskDTOfrom, TaskDTOfrom.class);
            ResponseEntity<TaskDTO> resultResponse = ResponseEntity.status(HttpStatus.CREATED).body(
                    new TaskDTO(response.getBody(), getUsernameById(response.getBody().getUserId()))
            );
            return resultResponse;
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private boolean hasDoneStatusChangedById(Long id, boolean statusDoneNow) {
        try {
            ResponseEntity<TaskDTOfrom> response =
                    restTemplate.getForEntity(externalBase + "/" + id, TaskDTOfrom.class);

            TaskDTOfrom taskDTOfrom = response.getBody();
            if (taskDTOfrom == null) {
                return false;
            }

            return taskDTOfrom.isDone() != statusDoneNow;

        } catch (RestClientException e) {
            return false;
        }
    }

    private boolean userExists(Long userId) {
        ResponseEntity<UserDTO> response = userController.getUserById(userId);
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO updatedTaskDTO) {
        try {
            if (userExists(updatedTaskDTO.getUserId())) {
                if (hasDoneStatusChangedById(id, updatedTaskDTO.isDone())) {
                    userController.recalculateUserPointsScore(
                            updatedTaskDTO.getUserId(),
                            updatedTaskDTO.getPointsForCompletion(),
                            updatedTaskDTO.isDone()
                    );
                }
            }

            TaskDTOfrom updatedTaskDTOfrom = new TaskDTOfrom(updatedTaskDTO);
            restTemplate.put(externalBase + "/" + id, updatedTaskDTOfrom);

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