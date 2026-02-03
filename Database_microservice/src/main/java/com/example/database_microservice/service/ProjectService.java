package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.ProjectDTO;
import com.example.database_microservice.model.Project;
import com.example.database_microservice.model.Task;
import com.example.database_microservice.repository.ProjectRepository;
import com.example.database_microservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    // --- Mapping ---
    private ProjectDTO toDTO(Project project) {
        return new ProjectDTO(project);
    }

    // --- CRUD ---
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        return projectRepository.findById(id).map(this::toDTO);
    }

    public Optional<Project> getProjectEntityById(Long id) {
        return projectRepository.findById(id);
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO);

        if (projectDTO.getTaskIds() != null && !projectDTO.getTaskIds().isEmpty()) {
            List<Task> tasks = taskRepository.findAllById(projectDTO.getTaskIds());
            for (Task task : tasks) {
                task.setProject(project);
            }
            project.setTasks(tasks);
        }

        return toDTO(projectRepository.save(project));
    }

    @Transactional
    public Optional<ProjectDTO> updateProject(Long id, ProjectDTO updatedProjectDTO) {
        return projectRepository.findById(id).map(project -> {
            // Update basic fields
            project.setProjectName(updatedProjectDTO.getProjectName());
            project.setDescription(updatedProjectDTO.getDescription());

            // Update task associations
            if (updatedProjectDTO.getTaskIds() != null) {
                // Remove project reference from old tasks
                List<Task> oldTasks = new ArrayList<>(project.getTasks());
                for (Task task : oldTasks) {
                    task.setProject(null);
                    taskRepository.save(task);
                }
                project.getTasks().clear();

                // Set new task associations
                if (!updatedProjectDTO.getTaskIds().isEmpty()) {
                    List<Task> newTasks = taskRepository.findAllById(updatedProjectDTO.getTaskIds());
                    for (Task task : newTasks) {
                        task.setProject(project);
                        taskRepository.save(task);
                    }
                    project.getTasks().addAll(newTasks);
                }
            }

            return toDTO(projectRepository.save(project));
        });
    }

    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
