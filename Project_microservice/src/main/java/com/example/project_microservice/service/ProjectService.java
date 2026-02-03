package com.example.project_microservice.service;

import com.example.project_microservice.DTOs.ProjectDTO;
import com.example.project_microservice.repository.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        return Optional.ofNullable(projectRepository.getProjectById(id))
                .map(ResponseEntity::getBody);
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        return projectRepository.createProject(projectDTO).getBody();
    }

    public ResponseEntity<ProjectDTO> updateProject(Long id, ProjectDTO updatedProjectDTO) {
        return projectRepository.updateProject(id, updatedProjectDTO);
    }

    public ResponseEntity<Void> deleteProject(Long id) {
        return projectRepository.deleteProject(id);
    }
}
