package com.project.makeup.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.makeup.dto.PostCreateDto;
import com.project.makeup.dto.PostDeleteDto;
import com.project.makeup.dto.PostReadDto;
import com.project.makeup.dto.PostUpdateDto;
import com.project.makeup.response.GenericResponse;
import com.project.makeup.service.PostService;

@CrossOrigin
@RestController
@RequestMapping("api/v1/post")
public class PostController {

    private final PostService postService;
    
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create post endpoint
    @PostMapping("/create")
    public ResponseEntity<?> createPost(
        @RequestParam("data") MultipartFile data,
        @RequestParam("userId") String userId,
        @RequestParam("text") String text) {
            var postCreateDto = PostCreateDto.builder()
                .data(data)
                .userId(userId)
                .text(text)
                .build();
        return ResponseEntity.ok(this.postService.createData(postCreateDto));
    }

    //read all posts endpoint
    @GetMapping("/all")
    public GenericResponse readAllPost() throws IOException{
        return this.postService.readAllPost();
    }

    //read post by id endpoint
    @PostMapping("/id")
    public GenericResponse readPost(@RequestBody PostReadDto postReadDto){
        return this.postService.readPost(postReadDto);
    }

    //update post endpoint
    @PostMapping("/update")
    public GenericResponse updatePost(@RequestBody PostUpdateDto postUpdateDto){
        return this.postService.updatePost(postUpdateDto);
    }


    //delete post endpoint
    @PostMapping("/delete")
    public GenericResponse deletePost(@RequestBody PostDeleteDto postDeleteDto){
        return this.postService.deletePost(postDeleteDto);
    }

    //get file endpoint
    @GetMapping(value = "/file", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getFile(@RequestParam String filePath,@RequestParam String userId) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(this.postService.downloadData(filePath,userId));
    }


}
