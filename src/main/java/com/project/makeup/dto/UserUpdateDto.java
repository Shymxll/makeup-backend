package com.project.makeup.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
        
        private String id;
        private String username;
        private String password;
        private String email;
        private String name;
        private String surname;
}
