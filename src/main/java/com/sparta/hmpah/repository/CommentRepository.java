package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Comment;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
  void deleteByPostId(Long postId);
  boolean existsByParentId(Long parentId);
  List<Comment> findAllByParentIdOrderByPositionDesc(Long parentId);
}
