package com.example.api_gateway.DTOs;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionDTO {

    private Long id;

    private String goal;
    private String type;       // INDIVIDUAL / TEAM
//    private String status;     // ACTIVE / FINISHED
//    private LocalDate startDate;
//    private LocalDate endDate;
}
