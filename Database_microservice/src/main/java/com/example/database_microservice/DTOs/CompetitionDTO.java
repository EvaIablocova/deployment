package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Competition;
import com.example.database_microservice.model.CompetitionStatus;
import com.example.database_microservice.model.CompetitionType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CompetitionDTO {
    private Long id;
    private String name;
    private String description;
    private Long groupId;
    private Long createdBy;
    private CompetitionType competitionType;
    private CompetitionStatus status;
    private Double targetValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CompetitionParticipantDTO> participants = new ArrayList<>();

    // Legacy fields for backward compatibility
    private String goal;
    private String type;
    private List<Long> userId;

    public CompetitionDTO() {}

    public CompetitionDTO(Competition competition) {
        this.id = competition.getId();
        this.name = competition.getName();
        this.description = competition.getDescription();
        this.groupId = competition.getGroupId();
        this.createdBy = competition.getCreatedBy();
        this.competitionType = competition.getCompetitionType();
        this.status = competition.getStatus();
        this.targetValue = competition.getTargetValue();
        this.startDate = competition.getStartDate();
        this.endDate = competition.getEndDate();
        this.createdAt = competition.getCreatedAt();
        this.updatedAt = competition.getUpdatedAt();

        // Map participants
        if (competition.getParticipants() != null) {
            this.participants = competition.getParticipants().stream()
                    .map(CompetitionParticipantDTO::new)
                    .collect(Collectors.toList());
            // Legacy userId list
            this.userId = competition.getParticipants().stream()
                    .map(p -> p.getUserId())
                    .collect(Collectors.toList());
        }

        // Legacy fields
        this.goal = competition.getGoal();
        this.type = competition.getType();
    }
}
