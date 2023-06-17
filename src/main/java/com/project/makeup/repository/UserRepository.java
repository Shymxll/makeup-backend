package com.project.makeup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.makeup.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByUsername(String username);
    
}
