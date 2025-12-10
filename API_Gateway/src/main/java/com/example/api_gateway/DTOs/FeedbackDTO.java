package com.example.api_gateway.DTOs;

import lombok.Data;
@Data
public class FeedbackDTO {
    private Long id;
    private String aspect;
    private String suggestion;


}