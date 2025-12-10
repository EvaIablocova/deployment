package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Lists;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ListDTO {
    private Long id;

    private String title;
    private String description;


    public ListDTO() { }

    public ListDTO(Lists list){
        this.id = list.getId();
        this.title = list.getTitle();
        this.description = list.getDescription();
    }

}