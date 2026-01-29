package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Task;
import jakarta.persistence.*;
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


    public TaskDTO() { }

    public TaskDTO(Task task){
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.dateToExecute = task.getDateToExecute();
        this.done = task.isDone();
        this.pointsForCompletion = task.getPointsForCompletion();
        if(task.getAssignedTo() != null){
            this.userId = task.getAssignedTo().getId();
        }
    }

}
