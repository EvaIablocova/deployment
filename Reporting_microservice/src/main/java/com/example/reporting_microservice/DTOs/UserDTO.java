package com.example.reporting_microservice.DTOs;

import lombok.Data;
    @Data
    public class UserDTO {

        private Long id;

        private String username;
        private String password;
}
