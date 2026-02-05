package com.example.task_microservice.DTOs;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class TaskDTO {

    private Long id;

    private String title;
    private String description;
    private LocalDateTime dateToExecute;
    private boolean done;
    private int pointsForCompletion;

    private Long userId;
    private Long projectId;

}