package com.project.makeup.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostReadResponse {
    
    private String id;
    private String text;
    private String imageUrl;

}
