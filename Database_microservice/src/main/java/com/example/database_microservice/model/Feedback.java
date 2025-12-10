package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.FeedbackDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String aspect;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    public Feedback(){}

    public Feedback(FeedbackDTO feedbackDTO){
        this.id = feedbackDTO.getId();
        this.aspect = feedbackDTO.getAspect();
        this.suggestion = feedbackDTO.getSuggestion();
    }

}