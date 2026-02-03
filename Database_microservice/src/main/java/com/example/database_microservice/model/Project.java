package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.ProjectDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String projectName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Task> tasks = new ArrayList<>();

    public Project(){}

    public Project(ProjectDTO projectDTO){
        this.id = projectDTO.getId();
        this.projectName = projectDTO.getProjectName();
        this.description = projectDTO.getDescription();
    }

}