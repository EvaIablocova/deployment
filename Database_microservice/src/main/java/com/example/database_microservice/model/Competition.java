package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.CompetitionDTO;
import com.example.database_microservice.DTOs.TaskDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "competition")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String goal;
    private String type;       // INDIVIDUAL / TEAM
//    private String status;     // ACTIVE / FINISHED
//    private LocalDate startDate;
//    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
            name = "competition_participants",
            joinColumns = @JoinColumn(name = "competition_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;


    public Competition(){}

    public Competition(CompetitionDTO CompetitionDTO){
        this.id = CompetitionDTO.getId();
        this.goal = CompetitionDTO.getGoal();
        this.type = CompetitionDTO.getType();
//        this.status = CompetitionDTO.getStatus();
//        this.startDate = CompetitionDTO.getStartDate();
//        this.endDate = CompetitionDTO.getEndDate();
    }
}
