package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Project;
import com.example.database_microservice.model.Task;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String description;
    private List<Long> taskIds = new ArrayList<>();

    public ProjectDTO() {}

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.projectName = project.getProjectName();
        this.description = project.getDescription();
        if (project.getTasks() != null) {
            this.taskIds = project.getTasks().stream()
                    .map(Task::getId)
                    .toList();
        }
    }
}
