package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.CommentLikeRequest;
import com.sparta.hmpah.dto.requestDto.CommentRequest;
import com.sparta.hmpah.dto.responseDto.CommentResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.CommentLike;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.CommentLikeRepository;
import com.sparta.hmpah.repository.CommentRepository;
import com.sparta.hmpah.repository.PostLikeRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import com.sparta.hmpah.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PostLikeRepository postLikeRepository;
  private final CommentLikeRepository commentLikeRepository;

  public List<CommentResponse> getComments(Long postId) { // 게시글 id를 기준으로 속해있는 모든 댓글을 가져옴

    List<Comment> commentList = commentRepository.findByPostIdOrderByCreatedAtAsc(
        postId);//.stream().map(CommentResponse::new).toList();
    for (Comment m : commentList) {
      System.out.println("m.getUser().toString() = " + m.getUser().toString());
      System.out.println("m.getPost().toString() = " + m.getPost().toString());
      System.out.println(
          "m.getCommentLikes().toString() = " + m.getCommentLikes().toString());
    }
    return commentList.stream().map(CommentResponse::new).toList();
  }

  public CommentResponse createComment(CommentRequest requestDto,
      UserDetailsImpl userDetails) { // 댓글 생성
    Post post = postRepository.findById(requestDto.getPostId()).orElseThrow();
    User user = userDetails.getUser();
    Comment comment = new Comment(requestDto, user, post);
    return new CommentResponse(commentRepository.save(comment));
  }

  @Transactional
  public Comment updateComment(Long id, CommentRequest requestDto,
      UserDetailsImpl userDetails) { //댓글 id를 기준으로 댓글 update
    Comment comment = findyComment(id);
    System.out.println(comment.toString());
    User user = userDetails.getUser();
    if (validateUsername(comment, user)) { // 작성자와 로그인한 user가 일치할 경우에만 업데이트
      comment.update(requestDto);
      System.out.println(comment.toString());
      return comment;
    } else {
      throw new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.");
    }
  }

  public Long deleteComment(Long id, UserDetailsImpl userDetails) { //댓글 id를 기준으로 댓글삭제
    Comment comment = findyComment(id);
    User user = userDetails.getUser();
    if (validateUsername(comment, user)) {
      commentRepository.delete(comment);
      return id;
    } else {
      return -id;
    }
  }

  public Comment findyComment(Long id) {// id를 기준으로 댓글 찾아옴
    return commentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("선택한 댓글은 존재하지 않습니다."));
  }

  public void deleteCommentByPostId(Long postId) { //post의 id를 기준으로 모든 댓글 삭제
    commentRepository.deleteByPostId(postId);
  }

  public boolean validateUsername(Comment comment, UserDetailsImpl userDetails) {
    if (comment.getUser().getUsername().equals(userDetails.getUsername())) {
      return true;
    }
    return false;
  }

  public boolean validateUsername(Comment comment, User userDetails) {
    if (comment.getUser().getUsername().equals(userDetails.getUsername())) {
      return true;
    }
    return false;
  }


  public Long countByCommentId(Long id) {
    return commentLikeRepository.countByCommentId(id);
  }

  public Long createCommentLike(CommentLikeRequest requestDto, UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    Long commentId = requestDto.getCommentId();
    Comment comment = commentRepository.findById(commentId).orElseThrow();
    if (commentLikeState(comment, user)) {
      commentLikeRepository.save(new CommentLike(comment, user));
    }
    return countByCommentId(commentId);
  }

  public Long deleteCommentLike(Long commentId, UserDetailsImpl userDetails) {//추천 삭제
    Long userId = userDetails.getUser().getId();
    if (existsByCommentIdAndUserId(commentId, userId)) {//추천이 존재할 경우에만 삭제
      CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
      commentLikeRepository.delete(commentLike);
    }
    return countByCommentId(commentId);
  }

  public boolean existsByCommentIdAndUserId(Long commentId, Long userId) { //이미 추천했다면 true 아니면 false
    return commentLikeRepository.existsByCommentIdAndUserId(commentId, userId);
  }

  public boolean checkWriter(Long commentId, String username) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 postId 입니다."));
    return username.equals(comment.getUser().getUsername());
  }

  public boolean commentLikeState(Comment comment, User user) {
    return (!existsByCommentIdAndUserId(comment.getId(), user.getId()) && !checkWriter(
        comment.getId(), user.getUsername()));
  }

}
