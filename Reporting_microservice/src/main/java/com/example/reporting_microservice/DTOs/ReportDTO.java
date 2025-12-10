package com.example.reporting_microservice.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {

    private Long userId;
    private String username;

    private List<CompetitionDTO> competitions;

    private List<TaskDTO> tasks;

    public ReportDTO(){}

    public ReportDTO(Long userId, String username,List<TaskDTO> tasksByUserId, List<CompetitionDTO> competitionsByUserId){
        this.userId = userId;
        this.username = username;
        this.tasks = tasksByUserId;
        this.competitions = competitionsByUserId;
    }

}