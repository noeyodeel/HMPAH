package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.CommentLikeRequest;
import com.sparta.hmpah.dto.requestDto.CommentRequest;
import com.sparta.hmpah.dto.responseDto.CommentResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/comments")
public class CommentController {

  private final CommentService commentService;

  @GetMapping()
  @Operation(summary = "postId를 기준으로 모든 댓글을 조회", description = "postId를 기준으로 모든 댓글을 조회합니다.")
  public List<CommentResponse> getComments(@RequestParam Long postId) {//postId기준 모든 댓글 조회
    return commentService.getComments(postId);
  }

  @PostMapping()//댓글 생성
  @Operation(summary = "댓글 생성", description = "postId와 User를 기준으로 댓글을 생성합니다.")
  public CommentResponse createComment(@RequestBody CommentRequest requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    log.info(userDetails.getUsername());
    log.info("create 호출");
    return commentService.createComment(requestDto, userDetails.getUser());
  }

  @PutMapping("/{id}")//댓글 수정
  @Operation(summary = "댓글 수정", description = "작성자와 일치하면 commentId를 기준으로 댓글수정합니다.")
  public CommentResponse updateComment(@PathVariable Long id, @RequestBody CommentRequest requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.updateComment(id, requestDto, userDetails.getUser());
  }

  @DeleteMapping("/{id}")//댓글 삭제
  @Operation(summary = "댓글 삭제", description = "작성자와 일치하면 commentId를 기준으로 댓글을 삭제합니다.")
  public List<CommentResponse> deleteComment(@PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.deleteComment(id, userDetails.getUser());
  }

  @GetMapping("/likes")
  @Operation(summary = "댓글 좋아요 조회", description = "commentId를 기준으로 댓글을 조회합니다.")
  public Long countByPostId(@RequestParam Long id) {
    return commentService.countByCommentId(id);
  }

  @PostMapping("/likes")//댓글 생성
  @Operation(summary = "댓글 좋아요 생성", description = "commentId를 기준으로 댓을의 좋아요를 생성합니다.")
  public Long createCommentLike(@RequestBody CommentLikeRequest requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    log.info("create 호출");
    return commentService.createCommentLike(requestDto, userDetails.getUser());
  }

  @DeleteMapping("/likes/{id}")
  @Operation(summary = "댓글 좋아요 삭제", description = "commentId를 기준으로 댓을의 좋아요를 삭제합니다.")
  public Long deleteCommentLike(@PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.deleteCommentLike(id, userDetails.getUser());
  }

}
