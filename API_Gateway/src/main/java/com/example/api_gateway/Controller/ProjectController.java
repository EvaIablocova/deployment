package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.ProjectDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/projects")
public class ProjectController {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://projectmicroservice:9018/api/projects";

    public ProjectController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    // --- CRUD ---

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        try {
            ResponseEntity<ProjectDTO[]> response = restTemplate.getForEntity(externalBase, ProjectDTO[].class);
            ProjectDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ProjectDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        try {
            ResponseEntity<ProjectDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ProjectDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        try {
            ResponseEntity<ProjectDTO> response =
                    restTemplate.postForEntity(externalBase, projectDTO, ProjectDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO updatedProjectDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedProjectDTO);
            return ResponseEntity.ok(updatedProjectDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
