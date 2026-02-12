package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.CompetitionParticipant;
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

    public CompetitionParticipantDTO() {}

    public CompetitionParticipantDTO(CompetitionParticipant participant) {
        this.id = participant.getId();
        if (participant.getCompetition() != null) {
            this.competitionId = participant.getCompetition().getId();
        }
        this.userId = participant.getUserId();
        this.username = participant.getUsername();
        this.currentValue = participant.getCurrentValue();
        this.rank = participant.getRank();
        this.isWinner = participant.isWinner();
        this.joinedAt = participant.getJoinedAt();
    }
}
