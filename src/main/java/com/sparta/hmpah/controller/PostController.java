package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Post", description = "Post API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @GetMapping
  @Operation(summary = "게시글 목록 조회(옵션)", description = "상태, 지역, 제목 옵션을 통해 게시글 목록을 조회한다.")
  public List<PostResponse> getPost(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) String title,
      @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostListByOption(status, location, title, userDetails.getUser());
  }

  @GetMapping("/follows")
  @Operation(summary = "게시글 목록 조회 (팔로우)", description = "팔로우 하는 유저의 게시글 목록을 조회한다.")
  public List<PostResponse> getPostListByFollow(@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostListByFollow(userDetails.getUser());
  }


  @GetMapping("/joins")
  @Operation(summary = "게시글 목록 조회 (참여)", description = "참여중인 게시글 목록을 조회한다.")
  public List<PostResponse> getPostListByMember(@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostListByMember(userDetails.getUser());
  }


  @GetMapping("/myposts")
  @Operation(summary = "게시글 목록 조회 (자신)", description = "자신의 게시글 목록을 조회한다.")
  public List<PostResponse> getMyPostList(@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getMyPostList(userDetails.getUser());
  }

  @GetMapping("/{postId}")
  @Operation(summary = "게시글 조회 (게시글 ID)", description = "게시글 ID를 통해 게시글을 조회한다.")
  public PostResponse getPostById(@PathVariable Long postId ,@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostById(postId, userDetails.getUser());
  }

  @PostMapping
  @Operation(summary = "게시글 작성", description = "게시글을 작성한다.")
  public PostResponse createPost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.createPost(postRequest, userDetails.getUser());
  }

  @PutMapping("/{postId}")
  @Operation(summary = "게시글 수정 (게시글 ID)", description = "게시글 ID를 통해 게시글을 수정한다.")
  public PostResponse updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.updatePost(postId, postRequest, userDetails.getUser());
  }

  @DeleteMapping("/{postId}")
  @Operation(summary = "게시글 삭제 (게시글 ID)", description = "게시글 ID를 통해 게시글을 삭제한다.")
  public String deletePost(@PathVariable Long postId ,@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.deletePost(postId, userDetails.getUser());
  }

  @PostMapping("/{postId}/likes")
  @Operation(summary = "게시글 좋아요", description = "게시글 ID를 통해 게시글을 좋아요 한다.")
  public String likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.likePost(postId, userDetails.getUser());
  }

  @PostMapping("/{postId}/joins")
  @Operation(summary = "모임 참여", description = "게시글 ID를 통해 게시글에 참여한다.")
  public String joinPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.joinPost(postId, userDetails.getUser());
  }

}
