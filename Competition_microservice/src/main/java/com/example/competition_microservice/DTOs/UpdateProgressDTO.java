package com.example.competition_microservice.DTOs;

import lombok.Data;

@Data
public class UpdateProgressDTO {
    private Long competitionId;
    private Long userId;
    private Double value;
    private boolean increment;
}
