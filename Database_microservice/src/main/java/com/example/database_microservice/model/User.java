package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.UserDTO;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String username;

    @Column(nullable = false, length = 150)
    private String password;

    @Column(name = "points_score", nullable = false)
    private int pointsScore = 0;

    public User(){}

    public User(UserDTO userDTO){
        this.id = userDTO.getId();
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.pointsScore = userDTO.getPointsScore();
    }

}