package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
