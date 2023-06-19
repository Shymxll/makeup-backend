package com.project.makeup.response;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostReadResponse {
    
    private String id;
    private String text;
    private String dataPath;

}
