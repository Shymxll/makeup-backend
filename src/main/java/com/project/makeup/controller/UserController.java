package com.project.makeup.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.makeup.dto.LoginDto;
import com.project.makeup.dto.UserCreateDto;
import com.project.makeup.dto.UserDeleteDto;
import com.project.makeup.dto.UserReadDto;
import com.project.makeup.dto.UserUpdateDto;
import com.project.makeup.response.GenericResponse;
import com.project.makeup.service.UserService;

import io.swagger.annotations.SwaggerDefinition;
import lombok.extern.java.Log;

@CrossOrigin
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/create")
    public GenericResponse createUser(@RequestBody UserCreateDto userCreateDto){

        
        return this.userService.createUser(userCreateDto);
    }

    @GetMapping("/all")
    public GenericResponse getUsers(){
        return this.userService.readAllUser();
    }

    @PostMapping("/id")
    public GenericResponse getUser(@RequestBody UserReadDto userReadDto){
       return this.userService.readUser(userReadDto);
      
    }

    @PostMapping("/login")
    public GenericResponse login(@RequestBody LoginDto loginDto){
        return this.userService.login(loginDto);
    }

    
    @PostMapping("/delete")
    public GenericResponse deleteUser(@RequestBody  UserDeleteDto userDeleteDto){
        return this.userService.deleteUser(userDeleteDto);
    }

    @PostMapping("/update")
    public GenericResponse updateUser(@RequestBody UserUpdateDto userUpdateDto){
        return this.userService.updateUser(userUpdateDto);
    }

    @GetMapping("/ranklist")
    public GenericResponse getRankList(){
        return this.userService.getRankList();
    }
    
}
