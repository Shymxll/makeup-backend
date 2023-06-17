package com.project.makeup.dto;

import lombok.Data;

@Data
public class PostUpdateDto {
    private Long id;
    private String type;
    private String text;
    private String image;
}
