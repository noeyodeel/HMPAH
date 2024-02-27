package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {


  void deleteAllByComment(Comment comment);

    Long countByCommentId(Long commnetId);

    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    CommentLike findByCommentIdAndUserId(Long commentId, long l);
}
