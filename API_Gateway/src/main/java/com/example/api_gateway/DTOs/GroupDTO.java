package com.example.api_gateway.DTOs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private String inviteCode;
    private LocalDateTime createdAt;
    private Long createdBy;
    private List<GroupMemberDTO> members;
}
