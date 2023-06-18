package com.project.makeup.service;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

import org.hibernate.mapping.List;
import org.springframework.stereotype.Service;

import com.project.makeup.dto.LoginDto;
import com.project.makeup.dto.UserCreateDto;
import com.project.makeup.dto.UserDeleteDto;
import com.project.makeup.dto.UserReadDto;
import com.project.makeup.dto.UserUpdateDto;
import com.project.makeup.entity.User;
import com.project.makeup.repository.UserRepository;
import com.project.makeup.response.GenericResponse;
import com.project.makeup.response.UserReadResponse;
import com.project.makeup.util.CryptUtil;



@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // new user creation
    public GenericResponse createUser(UserCreateDto userCreateDto) {

        // if usercreateDto is not empty then create user
        if (!userCreateDto.getName().equals("") && !userCreateDto.getSurname().equals("")
                && !userCreateDto.getEmail().equals("") && !userCreateDto.getPassword().equals("")
                && !userCreateDto.getUsername().equals("")) {
            try {
                User user = User.builder()
                        .name(userCreateDto.getName())
                        .surname(userCreateDto.getSurname())
                        .email(userCreateDto.getEmail())
                        .password(CryptUtil.encode(userCreateDto.getPassword()))
                        .username(userCreateDto.getUsername())
                        .role("USER")
                        .build();
                userRepository.save(user);
                return new GenericResponse("0", "User created", null);
            } catch (Exception e) {
                return new GenericResponse("1", "User not created", e.getMessage());
            }

        } else {
            return new GenericResponse("1", "Fill all fields", null);
        }

    }

    // user update
    public GenericResponse updateUser(UserUpdateDto userUpdateDto) {
        // decode id

        // check userUpdateDto is not empty elements
        if (!userUpdateDto.getId().equals("") && !userUpdateDto.getName().equals("")
                && !userUpdateDto.getSurname().equals("") && !userUpdateDto.getEmail().equals("")
                && !userUpdateDto.getPassword().equals("") && !userUpdateDto.getUsername().equals("")) {
            // get user from database
            try {
                long decodedId = Long.parseLong(CryptUtil.decode(userUpdateDto.getId().toString()));
                Optional<User> user = userRepository.findById(decodedId);
                // update user
                if (user.isPresent()) {
                    user.get().setName(userUpdateDto.getName());
                    user.get().setSurname(userUpdateDto.getSurname());
                    user.get().setEmail(userUpdateDto.getEmail());
                    user.get().setPassword(CryptUtil.encode(userUpdateDto.getPassword()));
                    user.get().setUsername(userUpdateDto.getUsername());
                    userRepository.save(user.get());
                    return new GenericResponse("0", "User updated", null);
                }
            } catch (Exception e) {
                return new GenericResponse("1", "User not updated", e.getMessage());
            }
        }

        return new GenericResponse("1", "Fill all fields", null);

    }

    // user deletion
    public GenericResponse deleteUser(UserDeleteDto userDeleteDto) {

        // delete user from database
        if (!userDeleteDto.getId().equals("")) {
            long decodedId = Long.parseLong(CryptUtil.decode(userDeleteDto.getId().toString()));
            // get user from database
            try {
                if (userRepository.findById(decodedId).get() != null) {
                    userRepository.deleteById(decodedId);
                    return new GenericResponse("0", "User deleted", null);
                } else {
                    return new GenericResponse("1", "User not found", null);
                }
            } catch (Exception e) {
                return new GenericResponse("1", "User not deleted", e.getMessage());
            }
        } else {
            return new GenericResponse("1", "Id cant be 0 or null", null);
        }
    }

    // user read
    public GenericResponse readAllUser() {
        ArrayList<UserReadResponse> usersResponseData = new ArrayList<UserReadResponse>();
        // read all users from database
        try {
            ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
            for (User user : users) {
                usersResponseData.add(UserReadResponse
                        .builder()
                        .name(user.getName())
                        .surname(user.getSurname())
                        .email(user.getEmail())
                        .username(user.getUsername()).build());
            }
            return new GenericResponse("0", "Users read", usersResponseData);
        } catch (Exception e) {
            return new GenericResponse("1", "Users not read", e.getMessage());
        }

    }

    // user read
    public GenericResponse readUser(UserReadDto userReadDto) {
        // read user from database
        if (!userReadDto.getId().equals("")) {
            System.out.println("id: " + userReadDto.getId());
            String id =  CryptUtil.decode(userReadDto.getId());
            System.out.println("id: " + id);
            //convert string to long
            long decodedId = Long.valueOf(id);
            try {
                User user = userRepository.findById(decodedId).get();
                UserReadResponse userResponseData = UserReadResponse
                        .builder()
                        .name(user.getName())
                        .surname(user.getSurname())
                        .email(user.getEmail())
                        .username(user.getUsername()).build();

                return new GenericResponse("0", "User read", userResponseData);
            } catch (Exception e) {
                return new GenericResponse("1", "User not read", e.getMessage());
            }
        } else {
            return new GenericResponse("1", "User not read", null);
        }

    }

    // login service
    public GenericResponse login(LoginDto loginDto) {
        if (!loginDto.getUsername().equals("") && !loginDto.getPassword().equals("")) {
            try {
                User user = userRepository.findByUsername(loginDto.getUsername());

                if (user != null) {
                    if (CryptUtil.decode(user.getPassword()).equals(loginDto.getPassword())) {
                        return new GenericResponse("0", "Login successful", CryptUtil.encode(user.getId().toString()));
                    } else {
                        return new GenericResponse("1", "Wrong password", null);
                    }
                } else {
                    return new GenericResponse("1", "User not found", null);
                }
            } catch (Exception e) {
                return new GenericResponse("1", "Login not successful", e.getMessage());
            }
        } else {
            return new GenericResponse("1", "Fill all fields", null);
        }
    }

   

}
