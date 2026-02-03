package com.example.project_microservice.repository;

import com.example.project_microservice.DTOs.ProjectDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepository {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://dbmicroservice:9009/db/projects";

    public ProjectRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<ProjectDTO> getAllProjects() {
        try {
            ResponseEntity<ProjectDTO[]> response = restTemplate.getForEntity(externalBase, ProjectDTO[].class);
            ProjectDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ProjectDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ProjectDTO> getProjectById(Long id) {
        try {
            ResponseEntity<ProjectDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ProjectDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ProjectDTO> createProject(ProjectDTO projectDTO) {
        try {
            ResponseEntity<ProjectDTO> response =
                    restTemplate.postForEntity(externalBase, projectDTO, ProjectDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<ProjectDTO> updateProject(Long id, ProjectDTO updatedProjectDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedProjectDTO);
            return ResponseEntity.ok(updatedProjectDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteProject(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
