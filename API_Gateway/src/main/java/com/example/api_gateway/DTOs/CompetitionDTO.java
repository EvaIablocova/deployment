package com.example.api_gateway.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
