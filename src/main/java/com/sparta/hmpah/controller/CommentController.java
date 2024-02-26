package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.CommentLikeRequest;
import com.sparta.hmpah.dto.requestDto.CommentRequest;
import com.sparta.hmpah.dto.responseDto.CommentLikeResponse;
import com.sparta.hmpah.dto.responseDto.CommentResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

  private final CommentService commentService;

  @GetMapping("/get-postId")
  public List<CommentResponse> getComments(@RequestParam Long postId) {//postId기준 모든 댓글 조회
    return commentService.getComments(postId);
  }

  @PostMapping("/create")//댓글 생성
  public CommentResponse createComment(@RequestBody CommentRequest requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    log.info(userDetails.getUsername());
    log.info("create 호출");
    return commentService.createComment(requestDto, userDetails);
  }

  @PutMapping("/{id}")//댓글 수정
  public Comment updateComment(@PathVariable Long id, @RequestBody CommentRequest requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.updateComment(id, requestDto, userDetails);
  }

  @DeleteMapping("/{id}")//댓글 삭제
  public Long deleteComment(@PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.deleteComment(id, userDetails);
  }

  @GetMapping("/get-like")
  public Long countByPostId(@RequestParam Long id) {
    return commentService.countByCommentId(id);
  }

  @PostMapping("/create-like")//댓글 생성
  public Long createCommentLike(@RequestBody CommentLikeRequest requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    log.info("create 호출");
    return commentService.createCommentLike(requestDto, userDetails);
  }

  @DeleteMapping("/delete-like/{id}")
  public Long deleteCommentLike(@PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.deleteCommentLike(id, userDetails);
  }


}
