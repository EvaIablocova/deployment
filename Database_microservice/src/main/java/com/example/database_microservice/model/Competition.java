package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.CompetitionDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "competition")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "created_by")
    private Long createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "competition_type", length = 20)
    private CompetitionType competitionType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompetitionStatus status = CompetitionStatus.PENDING;

    @Column(name = "target_value")
    private Double targetValue;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CompetitionParticipant> participants = new ArrayList<>();

    // Legacy fields for backward compatibility
    @Column
    private String goal;

    @Column
    private String type;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = CompetitionStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Competition() {}

    public Competition(CompetitionDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.groupId = dto.getGroupId();
        this.createdBy = dto.getCreatedBy();
        this.competitionType = dto.getCompetitionType();
        this.status = dto.getStatus() != null ? dto.getStatus() : CompetitionStatus.PENDING;
        this.targetValue = dto.getTargetValue();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        // Legacy fields
        this.goal = dto.getGoal();
        this.type = dto.getType();
    }

    public void addParticipant(CompetitionParticipant participant) {
        participants.add(participant);
        participant.setCompetition(this);
    }

    public void removeParticipant(CompetitionParticipant participant) {
        participants.remove(participant);
        participant.setCompetition(null);
    }
}
