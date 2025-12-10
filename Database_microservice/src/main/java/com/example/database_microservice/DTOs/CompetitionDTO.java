package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Competition;
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

    public CompetitionDTO() { }

    public CompetitionDTO(Competition competition){
        this.id = competition.getId();
        this.goal = competition.getGoal();
        this.type = competition.getType();
//        this.status = competition.getStatus();
//        this.startDate = competition.getStartDate();
//        this.endDate = competition.getEndDate();
        this.userId = competition.getParticipants().stream().map(user -> user.getId()).toList();
    }
}
