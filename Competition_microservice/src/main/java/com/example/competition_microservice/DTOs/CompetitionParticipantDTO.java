package com.example.competition_microservice.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompetitionParticipantDTO {
    private Long id;
    private Long competitionId;
    private Long userId;
    private String username;
    private Double currentValue;
    private Integer rank;
    private boolean isWinner;
    private LocalDateTime joinedAt;
}
