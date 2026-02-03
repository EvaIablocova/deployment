package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.TaskDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

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

    @Column(name = "date_to_execute")
    private LocalDateTime dateToExecute;

    @Column(name = "done", nullable = false)
    private boolean done = false;

    @Column(name = "points_for_completion", nullable = false)
    private int pointsForCompletion = 0;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Project project;

    public Task(){}

    public Task(TaskDTO taskDTO){
        this.id = taskDTO.getId();
        this.title = taskDTO.getTitle();
        this.description = taskDTO.getDescription();
        this.dateToExecute = taskDTO.getDateToExecute();
        this.done = taskDTO.isDone();
        this.pointsForCompletion = taskDTO.getPointsForCompletion();
    }


}

