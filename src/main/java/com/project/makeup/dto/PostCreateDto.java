package com.project.makeup.dto;

import lombok.Data;

@Data
public class PostCreateDto {
    private String type;
    private Long userId;
    private String text;
    private String image;
}
