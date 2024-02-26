package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.PostService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;


  @GetMapping("/all")
  @Operation(summary = "게시글 목록 조회 (전체)", description = "전체 게시글 목록을 조회한다.")
  public List<PostResponse> getPostList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.getPostList(userDetails.getUser());
  }

  @GetMapping("/status/{status}")
  @Operation(summary = "게시글 목록 조회 (상태)", description = "상태값을 통해 게시글 목록을 조회한다.")
  public List<PostResponse> getPostListByStatus(@PathVariable String status, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostListByStatus(status, userDetails.getUser());
  }

  @GetMapping("/locations/{location}")
  @Operation(summary = "게시글 목록 조회 (지역)", description = "지역을 통해 게시글 목록을 조회한다.")
  public List<PostResponse> getPostListByLocation(@PathVariable String location, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostListByLocation(location, userDetails.getUser());
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

  @GetMapping("/titles/{title}")
  @Operation(summary = "게시글 목록 조회 (제목)", description = "제목을 통해 게시글 목록을 조회한다.")
  public List<PostResponse> getPostListByTitle(@PathVariable String title ,@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostListByTitle(title, userDetails.getUser());
  }

  @GetMapping("/{postid}")
  @Operation(summary = "게시글 조회 (게시글 ID)", description = "ID를 통해 게시글을 조회한다.")
  public PostResponse getPostById(@PathVariable Long postid ,@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.getPostById(postid, userDetails.getUser());
  }

  @PostMapping
  @Operation(summary = "게시글 작성", description = "게시글을 작성한다.")
  public PostResponse createPost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.createPost(postRequest, userDetails.getUser());
  }

  @PutMapping("/{postid}")
  @Operation(summary = "게시글 수정 (게시글 ID)", description = "ID를 통해 게시글을 수정한다.")
  public PostResponse updatePost(@PathVariable Long postid, @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.updatePost(postid, postRequest, userDetails.getUser());
  }

  @DeleteMapping("/{postid}")
  @Operation(summary = "게시글 삭제 (게시글 ID)", description = "ID를 통해 게시글을 삭제한다.")
  public String deletePost(@PathVariable Long postid ,@AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.deletePost(postid, userDetails.getUser());
  }

  @PostMapping("/like/{postid}")
  @Operation(summary = "게시글 좋아요", description = "ID를 통해 게시글을 좋아요 한다.")
  public String likePost(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.likePost(postid, userDetails.getUser());
  }

  @PostMapping("/join/{postid}")
  @Operation(summary = "모임 참여", description = "ID를 통해 게시글에 참여한다.")
  public String joinPost(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return postService.joinPost(postid, userDetails.getUser());
  }

}
