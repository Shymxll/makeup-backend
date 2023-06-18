package com.project.makeup.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        @RequestParam("image") MultipartFile image,
        @RequestParam("userId") String userId,
        @RequestParam("text") String text) {
            var postCreateDto = PostCreateDto.builder()
                .image(image)
                .userId(userId)
                .text(text)
                .build();
        return ResponseEntity.ok(this.postService.createPhoto(postCreateDto));
    }

    //read all posts endpoint
    @GetMapping("/all")
    public GenericResponse readAllPost(){
        return this.postService.readAllPost();
    }

    //read post by id endpoint
    public GenericResponse readPost(@RequestParam PostReadDto postReadDto){
        return this.postService.readPost(postReadDto);
    }
    @GetMapping("/name")
    public ResponseEntity<?> readPostByPostName(){
        return ResponseEntity.status(HttpStatus.OK).contentType(ImageJP).body(this.postService.downloadImage("nahid_1687089616573"));
    }

    //update post endpoint
    public GenericResponse updatePost(@RequestBody PostUpdateDto postUpdateDto){
        return this.postService.updatePost(postUpdateDto);
    }

    //delete post endpoint
    public GenericResponse deletePost(@RequestBody PostDeleteDto postDeleteDto){
        return this.postService.deletePost(postDeleteDto);
    }
}
