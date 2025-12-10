package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.TaskDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    public Task(){}

    public Task(TaskDTO taskDTO){
        this.id = taskDTO.getId();
        this.title = taskDTO.getTitle();
        this.description = taskDTO.getDescription();
    }

}

