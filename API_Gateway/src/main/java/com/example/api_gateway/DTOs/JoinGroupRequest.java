package com.example.api_gateway.DTOs;

import lombok.Data;

@Data
public class JoinGroupRequest {
    private String inviteCode;
    private Long userId;
}
