package com.project.makeup.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostUpdateDto {
    private String id;
    private String text;
    private MultipartFile data;
}
