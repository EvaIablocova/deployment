package com.example.api_gateway.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {

    private Long userId;
    private String username;

    private List<CompetitionDTO> competitions;

    private List<TaskDTO> tasks;


}