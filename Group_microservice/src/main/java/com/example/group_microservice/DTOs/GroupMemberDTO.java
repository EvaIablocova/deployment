package com.example.group_microservice.DTOs;

import lombok.Data;

@Data
public class GroupMemberDTO {
    private Long id;
    private String username;
    private int pointsScore;
    private String role;      // USER or PREMIUM_USER
    private boolean isOwner;  // true if this user created the group
}
