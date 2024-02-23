package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByPost(Post post);
}
