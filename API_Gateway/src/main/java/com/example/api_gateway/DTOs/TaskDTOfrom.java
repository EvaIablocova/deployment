package com.example.api_gateway.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskDTOfrom {
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dateToExecute;
    private boolean done;
    private int pointsForCompletion;

    private Long userId;
    private Long projectId;

    public TaskDTOfrom(TaskDTO taskDTO) {
        this.id = taskDTO.getId();
        this.title = taskDTO.getTitle();
        this.description = taskDTO.getDescription();
        this.dateToExecute = taskDTO.getDateToExecute();
        this.done = taskDTO.isDone();
        this.pointsForCompletion = taskDTO.getPointsForCompletion();
        this.userId = taskDTO.getUserId();
        this.projectId = taskDTO.getProjectId();
    }

}