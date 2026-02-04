package com.example.auth_microservice.DTOs;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String token;
}
