package com.example.api_gateway.DTOs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}