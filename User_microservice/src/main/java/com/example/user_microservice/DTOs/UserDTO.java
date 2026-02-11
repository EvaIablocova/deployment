package com.example.user_microservice.DTOs;

import lombok.Data;
@Data
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private int pointsScore;
    private Long groupId;
    private String role;  // USER or PREMIUM_USER
}