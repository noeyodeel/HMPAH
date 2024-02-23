package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
