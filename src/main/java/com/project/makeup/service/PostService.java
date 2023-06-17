package com.project.makeup.service;

import org.springframework.stereotype.Service;

import com.project.makeup.entity.Post;
import com.project.makeup.repository.PostRepository;

@Service
public class PostService {
    
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //new post creation
    public String createPost(){
        return "Post created";
    }

    //post update
    public void updatePost(){
        System.out.println("Post updated");
    }

    //post deletion
    public void deletePost(){
        System.out.println("Post deleted");
    }

    //post read
    public void readPost(){
        System.out.println("Post read");
    }
    

    
}
