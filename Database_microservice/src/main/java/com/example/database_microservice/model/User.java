package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150, unique = true)
    private String username;

    @Column(nullable = false, length = 150)
    private String password;

    @Column(name = "points_score", nullable = false)
    private int pointsScore = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 20)
    private UserRole role = UserRole.USER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private UserGroup userGroup;

    public User() {}

    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.pointsScore = userDTO.getPointsScore();
        if (userDTO.getRole() != null) {
            this.role = UserRole.valueOf(userDTO.getRole());
        }
    }

    public Long getGroupId() {
        return userGroup != null ? userGroup.getId() : null;
    }
}