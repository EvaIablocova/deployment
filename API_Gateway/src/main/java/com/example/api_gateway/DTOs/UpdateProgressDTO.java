package com.example.api_gateway.DTOs;

import lombok.Data;

@Data
public class UpdateProgressDTO {
    private Long competitionId;
    private Long userId;
    private Double value;
    private boolean increment;
}
