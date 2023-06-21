package com.project.makeup.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
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

    public String uploadPhoto(MultipartFile file, String imageName) throws IOException {
        String directoryPath = "src/main/resources/file/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Yeni klasör oluşturur
        }
        // type file
        String type = file.getContentType().split("/")[1];
        System.out.println(type);
        String newName = imageName + "." + type;
        System.out.println(newName);
        String filePath = directoryPath + imageName + "." + file.getContentType().split("/")[1];
        File tardownloadData = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(tardownloadData);
        FileCopyUtils.copy(file.getInputStream(), outputStream);
        outputStream.close();
        return filePath;
    }

    // post creation
    public GenericResponse createData(PostCreateDto postCreateDto) {
        String text = postCreateDto.getText();
        String dataPath = "";
        String imageName = "";
        // decode user id from postCreateDto and find user by id
        long decodedId = Long.parseLong(CryptUtil.decode(postCreateDto.getUserId()));
        Optional<User> user = this.userRepository.findById(decodedId);
        if (user.isPresent()) {
            // if text is empty and image is not empty
            if (text.equals("") && postCreateDto.getData().isEmpty()) {
                return new GenericResponse("1", "Fill all fields", null);
            }
            try {
                if(postCreateDto.getText().equals("")){
                // create image name
                imageName = user.get().getName() + "_" + System.currentTimeMillis();
                // get image path
                dataPath = uploadPhoto(postCreateDto.getData(), imageName);
                }

                var post = Post.builder()
                        .user(user.get())
                        .text(postCreateDto.getText())
                        .dataPath(text.equals("") ? dataPath : null)
                        .build();

                var upgradeUser = User.builder()
                        .id(user.get().getId())
                        .name(user.get().getName())
                        .surname(user.get().getSurname())
                        .email(user.get().getEmail())
                        .password(user.get().getPassword())
                        .username(user.get().getUsername())
                        .score(user.get().getScore()+1)
                        .role(user.get().getRole())
                        .build();

                
                // check if uploaded
                if (uploadPhoto(postCreateDto.getData(), imageName).equals("null")) {
                    System.out.println("image: Lets");
                    return new GenericResponse("1", "Data not upload", null);
                }

                this.postRepository.save(post);
                this.userRepository.save(upgradeUser);
                return new GenericResponse("0", "Post created", null);
            } catch (Exception e) {
                return new GenericResponse("1", "Post not created", e.getMessage());
            }
        }

        return new GenericResponse("1", "User Not Found", null);

    }

    // post update

    public GenericResponse updatePost(PostUpdateDto postUpdateDto) {
        String id = CryptUtil.decode(postUpdateDto.getId());
        String dataPath = "";
        Optional<Post> post = this.postRepository.findById(Long.parseLong(id));
        if (post.isPresent()) {
            try {
                // if text is empty and image is not empty
                if (postUpdateDto.getText().equals("") && postUpdateDto.getData().isEmpty()) {
                    return new GenericResponse("1", "Fill all fields", null);
                }
                // create image name
                if(!postUpdateDto.getData().isEmpty()){
                    String imageName = post.get().getUser().getName() + "_" + System.currentTimeMillis();
                    // get image path
                  dataPath = uploadPhoto(postUpdateDto.getData(), imageName);
                }
                
                post.get().setText(postUpdateDto.getText());
                post.get().setDataPath(postUpdateDto.getText().equals("") ? dataPath : null);
                this.postRepository.save(post.get());
                return new GenericResponse("0", "Post updated", null);
            } catch (Exception e) {
                return new GenericResponse("1", "Post not updated", e.getMessage());
            }
        } else {
            return new GenericResponse("1", "Post not found", null);
        }
    }

    // post deletion
    public GenericResponse deletePost(PostDeleteDto postDeleteDto) {
        String id = CryptUtil.decode(postDeleteDto.getId());
        Optional<Post> post = this.postRepository.findById(Long.parseLong(id));
        if (post.isPresent()) {
            this.postRepository.delete(post.get());
            //delete file from directory
            try {
                 File file = new File(post.get().getDataPath());
                 file.delete();
            } catch (Exception e) {
               return new GenericResponse("1", "Data not found", null);
            }
           
            return new GenericResponse("0", "Post deleted", null);
        } else {
            return new GenericResponse("1", "Post not found", null);
        }
    }

    // post read
    public GenericResponse readPost(PostReadDto postReadDto) {
       String id = CryptUtil.decode(postReadDto.getId());
         Optional<Post> post = this.postRepository.findById(Long.parseLong(id));
            if (post.isPresent()) {
                PostReadResponse postReadResponse = PostReadResponse.builder()
                        .id(CryptUtil.encode(post.get().getId().toString()))
                        .text(post.get().getText().equals("") ? null : post.get().getText())
                        .dataPath(post.get().getText().equals("") ? post.get().getDataPath() : null)
                        .build();
                return new GenericResponse("0", "Post found", postReadResponse);
            } else {
                return new GenericResponse("1", "Post not found", null);
            }
    }

    public GenericResponse readAllPost() throws IOException {
        List<Post> posts = postRepository.findAll();
        if (!posts.isEmpty()) {
            List<PostReadResponse> postReadResponses = new ArrayList<>();
            for (Post post : posts) {
                PostReadResponse postReadResponse = PostReadResponse.builder()
                        .id(CryptUtil.encode(post.getId().toString()))
                        .text(post.getText().equals("") ? null : post.getText())
                        .dataPath(post.getText().equals("") ? post.getDataPath() : null)
                        .build();
                postReadResponses.add(postReadResponse);
            }
            return new GenericResponse("0", "Posts found", postReadResponses);
        } else {
            return new GenericResponse("1", "No post found", null);
        }

    }

    public byte[] downloadData(String filePath,String userId) throws IOException {
        String directoryPath = "src/main/resources/file/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Yeni klasör oluşturur
        }

        File file = new File(filePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        // decode user id from postCreateDto and find user by id
        long decodedId = Long.parseLong(CryptUtil.decode(userId));
        Optional<User> user = this.userRepository.findById(decodedId);
        //increase user score
        var upgradeUser = User.builder()
                        .id(user.get().getId())
                        .name(user.get().getName())
                        .surname(user.get().getSurname())
                        .email(user.get().getEmail())
                        .password(user.get().getPassword())
                        .username(user.get().getUsername())
                        .score(user.get().getScore()+1)
                        .role(user.get().getRole())
                        .build();
        this.userRepository.save(upgradeUser);

        return fileContent;
    }



}
