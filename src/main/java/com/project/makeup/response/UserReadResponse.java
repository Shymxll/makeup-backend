package com.project.makeup.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReadResponse {
        
    private String name;
    private String surname;
    private String username;
    private String email;

}
