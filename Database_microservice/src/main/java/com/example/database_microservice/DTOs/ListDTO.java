package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.ListItem;
import com.example.database_microservice.model.ListType;
import com.example.database_microservice.model.Lists;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListDTO {
    private Long id;
    private String title;
    private String description;
    private Long groupId;
    private Long createdBy;
    private ListType listType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ListItemDTO> items = new ArrayList<>();

    public ListDTO() { }

    public ListDTO(Lists list){
        this.id = list.getId();
        this.title = list.getTitle();
        this.description = list.getDescription();
        this.groupId = list.getGroupId();
        this.createdBy = list.getCreatedBy();
        this.listType = list.getListType();
        this.createdAt = list.getCreatedAt();
        this.updatedAt = list.getUpdatedAt();
        if (list.getItems() != null) {
            this.items = list.getItems().stream()
                    .map(ListItemDTO::new)
                    .collect(Collectors.toList());
        }
    }

}