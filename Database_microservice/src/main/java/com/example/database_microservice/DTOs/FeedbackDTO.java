package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Feedback;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class FeedbackDTO {
    private Long id;
    private String aspect;
    private String suggestion;

    public FeedbackDTO(){}

    public FeedbackDTO(Feedback feedback){
        this.id = feedback.getId();
        this.aspect = feedback.getAspect();
        this.suggestion = feedback.getSuggestion();
    }

}