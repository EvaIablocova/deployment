package com.example.database_microservice.DTOs;

import lombok.Data;

@Data
public class UpdateProgressDTO {
    private Long competitionId;
    private Long userId;
    private Double value;
    private boolean increment; // If true, add to current value; if false, set to value

    public UpdateProgressDTO() {}

    public UpdateProgressDTO(Long competitionId, Long userId, Double value, boolean increment) {
        this.competitionId = competitionId;
        this.userId = userId;
        this.value = value;
        this.increment = increment;
    }
}
