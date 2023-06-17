package com.project.makeup.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
}
