package com.example.competition_microservice.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CompetitionDTO {

    private Long id;

    private String goal;
    private String type;       // INDIVIDUAL / TEAM
//    private String status;     // ACTIVE / FINISHED
//    private LocalDate startDate;
//    private LocalDate endDate;

    private List<Long> userId;


}