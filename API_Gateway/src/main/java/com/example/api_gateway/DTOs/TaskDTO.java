package com.example.api_gateway.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskDTO {
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dateToExecute;
    private boolean done;
    private int pointsForCompletion;

    private String assignedToUsername;
    private Long userId;

    private Long projectId;

    public TaskDTO(TaskDTOfrom taskDTOfrom, String assignedToUsername) {
        this.id = taskDTOfrom.getId();
        this.title = taskDTOfrom.getTitle();
        this.description = taskDTOfrom.getDescription();
        this.dateToExecute = taskDTOfrom.getDateToExecute();
        this.done = taskDTOfrom.isDone();
        this.pointsForCompletion = taskDTOfrom.getPointsForCompletion();
        this.userId = taskDTOfrom.getUserId();
        this.assignedToUsername = assignedToUsername;
        this.projectId = taskDTOfrom.getProjectId();
    }

}