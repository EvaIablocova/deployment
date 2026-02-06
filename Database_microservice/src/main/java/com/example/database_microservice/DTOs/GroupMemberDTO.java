package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.User;
import lombok.Data;

@Data
public class GroupMemberDTO {

    private Long id;
    private String username;
    private int pointsScore;
    private String role;      // USER or PREMIUM_USER
    private boolean isOwner;  // true if this user created the group

    public GroupMemberDTO() { }

    public GroupMemberDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.pointsScore = user.getPointsScore();
        this.role = user.getRole() != null ? user.getRole().name() : "USER";
    }

    public GroupMemberDTO(User user, Long groupCreatedBy) {
        this(user);
        this.isOwner = user.getId().equals(groupCreatedBy);
    }
}
