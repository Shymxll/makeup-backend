package com.project.makeup.repository;

import com.project.makeup.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{
    
}
