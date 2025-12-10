package com.example.feedback_microservice.DTOs;

import lombok.Data;
@Data
public class FeedbackDTO {
    private Long id;
    private String aspect;
    private String suggestion;


}