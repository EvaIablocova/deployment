package com.example.api_gateway.DTOs;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String username;
    private Long userId;
}
