package com.project.makeup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.makeup.dto.PostCreateDto;
import com.project.makeup.dto.PostDeleteDto;
import com.project.makeup.dto.PostReadDto;
import com.project.makeup.dto.PostUpdateDto;
import com.project.makeup.entity.Post;
import com.project.makeup.entity.User;
import com.project.makeup.repository.PostRepository;
import com.project.makeup.repository.UserRepository;
import com.project.makeup.response.GenericResponse;
import com.project.makeup.response.PostReadResponse;
import com.project.makeup.util.CryptUtil;
import com.project.makeup.util.ImageUtil;


import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public String uploadPhoto(MultipartFile file, String username, String currentMillis) throws IOException {
        String folderPath = "src/main/resources/file";

        File folder = new File(folderPath);

        if (!folder.exists()) {
            boolean created = folder.mkdirs();
        }
        Path filePath = Path.of(folderPath, username + "_" + currentMillis);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();

    }

    // post creation
    public GenericResponse createPhoto(PostCreateDto postCreateDto) {
        String text = postCreateDto.getText();
        //decode user id from postCreateDto and find user by id
        long decodedId = Long.parseLong(CryptUtil.decode(postCreateDto.getUserId()));
        Optional<User> user = this.userRepository.findById(decodedId);
        if (user.isPresent()) {
            // if text is empty and image is not empty
            if(text.equals("") && postCreateDto.getImage().isEmpty()){
                return new GenericResponse("1", "Fill all fields", null);
            }
            try {
                //
                var post = Post.builder()
                        .user(user.get())
                        .text(postCreateDto.getText())
                        .imageName(text.equals("") ? (user.get().getName() + "_" + System.currentTimeMillis()) : "")   // if text is empty then image name will be user name + current time
                        .imageData(text.equals("") ? ImageUtil.compressImage(postCreateDto.getImage().getBytes()) : null)// if text is empty then image data will be compressed image
                        .build();
                this.postRepository.save(post);
                return new GenericResponse("0", "Post created", null);
            } catch (Exception e) {
                return new GenericResponse("1", "Post not created", e.getMessage());
            }
        }

        return new GenericResponse("1", "User Not Found", null);

    }

    public byte[] downloadImage(String imageName) {
        Optional<Post> dbImageData = this.postRepository.findByImageName(imageName);
        byte[] images = ImageUtil.decompressImage(dbImageData.get().getImageData());
        return images;
    }

    // post update

    public GenericResponse updatePost(PostUpdateDto postUpdateDto) {
        return null;
    }

    // post deletion
    public GenericResponse deletePost(PostDeleteDto postDeleteDto) {
        return null;
    }

    // post read
    public GenericResponse readPost(PostReadDto postReadDto) {
        return null;
    }

    public GenericResponse readAllPost() {
        List<Post> posts = postRepository.findAll();
        if(!posts.isEmpty()){
            List<PostReadResponse> postReadResponses = new ArrayList<>();
            for(Post post : posts){
                PostReadResponse postReadResponse = PostReadResponse.builder()
                    .id(CryptUtil.encode(post.getId().toString()))
                    .text(post.getText().equals("") ? null : post.getText())
                    .imageUrl("url?")
                    .build();
                postReadResponses.add(postReadResponse);
            }
            return new GenericResponse("0", "Posts found", postReadResponses);
        }else{
            return new GenericResponse("1", "No post found", null);
        }
    }

}
