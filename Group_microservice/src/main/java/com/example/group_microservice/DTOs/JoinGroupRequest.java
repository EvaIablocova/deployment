package com.example.group_microservice.DTOs;

import lombok.Data;

@Data
public class JoinGroupRequest {
    private String inviteCode;
    private Long userId;
}
