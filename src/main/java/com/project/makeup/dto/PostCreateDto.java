package com.project.makeup.dto;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreateDto {
    private String userId;
    private String text;
    private MultipartFile data;
}
