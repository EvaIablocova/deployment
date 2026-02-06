package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.GroupDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user_groups")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "invite_code", unique = true, length = 8)
    private String inviteCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> members = new ArrayList<>();

    public UserGroup() {
        this.createdAt = LocalDateTime.now();
    }

    public UserGroup(GroupDTO groupDTO) {
        this.id = groupDTO.getId();
        this.name = groupDTO.getName();
        this.inviteCode = groupDTO.getInviteCode();
        this.createdAt = groupDTO.getCreatedAt() != null ? groupDTO.getCreatedAt() : LocalDateTime.now();
        this.createdBy = groupDTO.getCreatedBy();
    }
}
