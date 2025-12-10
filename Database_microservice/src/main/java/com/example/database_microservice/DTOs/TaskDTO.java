package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Task;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class TaskDTO {
    private Long id;

    private String title;
    private String description;

    private Long userId;


    public TaskDTO() { }

    public TaskDTO(Task task){
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        if(task.getAssignedTo() != null){
            this.userId = task.getAssignedTo().getId();
        }
    }

}
