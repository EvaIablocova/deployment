package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.ListItemDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "list_item")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String textOfItem;

    @Column(name = "done", nullable = false)
    private boolean done = false;

    public ListItem(){}

    public ListItem(ListItemDTO listItemDTO){
        this.id = listItemDTO.getId();
        this.textOfItem = listItemDTO.getTextOfItem();
        this.done = listItemDTO.isDone();
    }

}