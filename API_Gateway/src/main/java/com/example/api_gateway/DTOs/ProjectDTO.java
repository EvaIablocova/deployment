package com.example.api_gateway.DTOs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String description;
    private List<Long> taskIds = new ArrayList<>();
}
