package com.sparta.hmpah.post;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.entity.LocationEnum;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.CommentLikeRepository;
import com.sparta.hmpah.repository.CommentRepository;
import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PostLikeRepository;
import com.sparta.hmpah.repository.PostMemberRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.service.PostService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
  @Mock
  PostRepository postRepository;
  @Mock
  CommentRepository commentRepository;
  @Mock
  PostLikeRepository postLikeRepository;
  @Mock
  CommentLikeRepository commentLikeRepository;
  @Mock
  PostMemberRepository postMemberRepository;
  @Mock
  FollowRepository followRepository;

  @Test
  @DisplayName("updatePost 테스트 (성공)")
  void updatePostTest(){
    //given
    Long postId = 100L;
    User user = new User();
    user.setId(100L);
    PostRequest postRequest = new PostRequest(
        "title",
        "content",
        "GANGNAM",
        5
    );

    PostRequest newPostRequest = new PostRequest(
        "title1",
        "content1",
        "HONGDAE",
        5
    );

    PostService postService = new PostService(
        postRepository,
        commentRepository,
        postLikeRepository,
        commentLikeRepository,
        postMemberRepository,
        followRepository
        );

    Post post = new Post(postRequest, user);

    given(postRepository.findById(postId)).willReturn(Optional.of(post));

    //when
    PostResponse response = postService.updatePost(postId, newPostRequest, user);

    //then
    assertEquals(response.getTitle(),"title1");
    assertEquals(response.getContent(),"content1");
    assertEquals(response.getLocation(), "HONGDAE");
    assertEquals(response.getStatus(), "RECRUTING");
  }

  @Test
  @DisplayName("updatePost 테스트 (실패)")
  void updatePostFailTest(){
    //given
    Long postId = 100L;
    User user = new User();
    user.setId(100L);
    User user1 = new User();
    user1.setId(200L);
    PostRequest postRequest = new PostRequest(
        "title",
        "content",
        "GANGNAM",
        5
    );

    PostRequest newPostRequest = new PostRequest(
        "title1",
        "content1",
        "HONGDAE",
        5
    );

    PostService postService = new PostService(
        postRepository,
        commentRepository,
        postLikeRepository,
        commentLikeRepository,
        postMemberRepository,
        followRepository
    );

    Post post = new Post(postRequest, user);
    given(postRepository.findById(postId)).willReturn(Optional.of(post));

    //when
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      postService.updatePost(postId, newPostRequest, user1);
    });

    //then
    assertEquals("해당 게시글을 수정할 권한이 없습니다.", exception.getMessage());
  }

}
