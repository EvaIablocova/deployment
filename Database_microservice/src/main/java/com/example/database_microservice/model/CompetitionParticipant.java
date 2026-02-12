package com.example.database_microservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "competition_participant")
public class CompetitionParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    @JsonIgnore
    private Competition competition;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 100)
    private String username;

    @Column(name = "current_value", nullable = false)
    private Double currentValue = 0.0;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "is_winner", nullable = false)
    private boolean isWinner = false;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
        if (currentValue == null) {
            currentValue = 0.0;
        }
    }

    public CompetitionParticipant() {}

    public CompetitionParticipant(Long userId, String username) {
        this.userId = userId;
        this.username = username;
        this.currentValue = 0.0;
        this.isWinner = false;
    }
}
