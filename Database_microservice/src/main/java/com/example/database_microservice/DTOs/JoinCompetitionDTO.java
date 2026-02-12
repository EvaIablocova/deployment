package com.example.database_microservice.DTOs;

import lombok.Data;

@Data
public class JoinCompetitionDTO {
    private Long userId;
    private String username;

    public JoinCompetitionDTO() {}

    public JoinCompetitionDTO(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
