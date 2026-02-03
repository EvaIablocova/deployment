package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.ListItem;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ListItemDTO {
    private Long id;

    private String textOfItem;
    private boolean done;


    public ListItemDTO() { }

    public ListItemDTO(ListItem listItem){
        this.id = listItem.getId();
        this.textOfItem = listItem.getTextOfItem();
        this.done = listItem.isDone();
    }

}