package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.User;
import lombok.Data;
@Data
public class UserDTO {

    private Long id;

    private String username;
    private String password;

    public UserDTO() { }

    public UserDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

}
