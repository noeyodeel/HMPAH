package com.sparta.hmpah.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.UserRepository;
import com.sparta.hmpah.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostServiceIntegrationTest {

  @Autowired
  PostService postService;

  @Autowired
  UserRepository userRepository;

  User user;
  PostResponse createdPost = null;

  @Test
  @Order(1)
  @DisplayName("createPost 테스트")
  void createPostTest(){
    //given
    String title = "title";
    String content = "content";
    String location = "GANGNAM";
    Integer maxCount = 5;
    PostRequest postRequest = new PostRequest(title, content, location, maxCount);
    user = userRepository.findById(1L).orElse(null);

    //when
    PostResponse postResponse = postService.createPost(postRequest, user);

    //then
    assertNotNull(postResponse.getId());
    assertEquals(title, postResponse.getTitle());
    assertEquals(content, postResponse.getContent());
    assertEquals(location, postResponse.getLocation());
    assertEquals(maxCount, postResponse.getMaxCount());
    assertEquals(1, postResponse.getCurrentCount());
    assertEquals(true, postResponse.getIsMember());
    createdPost = postResponse;
  }

  @Test
  @Order(2)
  @DisplayName("createPost 실패 테스트(모집인원)")
  void createPostMaxCountTest(){
    //given
    String title = "title";
    String content = "content";
    String location = "GANGNAM";
    Integer maxCount = 0;
    PostRequest postRequest = new PostRequest(title, content, location, maxCount);
    user = userRepository.findById(1L).orElse(null);

//when
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      postService.createPost(postRequest, user);
    });

    //then
    assertEquals("모집인원은 0보다 커야합니다.", exception.getMessage());
  }

  @Test
  @Order(3)
  @DisplayName("updatePost 실패 테스트 (권한)")
  void updatePostPermissionTest(){
    //given
    Long postId = this.createdPost.getId();
    String newTitle = "new title";
    String newContent = "new content";
    String newLocation = "HONGDAE";
    Integer newMaxCount = 4;
    PostRequest postRequest = new PostRequest(newTitle, newContent, newLocation, newMaxCount);
    user = userRepository.findById(2L).orElse(null);

    //when
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      postService.updatePost(postId, postRequest, user);
    });

    //then
    assertEquals("해당 게시글을 수정할 권한이 없습니다.", exception.getMessage());
  }

  @Test
  @Order(4)
  @DisplayName("updatePost 실패 테스트 (모집인원)")
  void updatePostMaxCountTest(){
    //given
    Long postId = this.createdPost.getId();
    String newTitle = "new title";
    String newContent = "new content";
    String newLocation = "HONGDAE";
    Integer newMaxCount = 0;
    PostRequest postRequest = new PostRequest(newTitle, newContent, newLocation, newMaxCount);
    user = userRepository.findById(1L).orElse(null);

    //when
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      postService.updatePost(postId, postRequest, user);
    });

    //then
    assertEquals("모집인원은 현재 인원보다 커야 합니다.", exception.getMessage());
  }

  @Test
  @Order(5)
  @DisplayName("likePost 좋아요 테스트")
  void likePostTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(2L).orElse(null);

    //when
    String likeResponse = postService.likePost(postId, user);

    //then
    assertEquals(likeResponse, "게시물에 좋아요를 누르셨습니다.");

  }

  @Test
  @Order(6)
  @DisplayName("likePost 좋아요 취소 테스트")
  void unLikePostTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(2L).orElse(null);

    //when
    String likeResponse = postService.likePost(postId, user);

    //then
    assertEquals(likeResponse, "게시물에 좋아요를 취소합니다.");

  }

  @Test
  @Order(7)
  @DisplayName("likePost 실패 테스트")
  void LikePostFailTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(1L).orElse(null);

//when
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      postService.likePost(postId, user);
    });

    //then
    assertEquals("자신의 게시물에는 좋아요를 할 수 없습니다.", exception.getMessage());

  }

  @Test
  @Order(8)
  @DisplayName("joinPost 테스트")
  void joinPostTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(2L).orElse(null);

    //when
    String joinResponse = postService.joinPost(postId, user);

    //then
    assertEquals(joinResponse, "게시물에 참여하셨습니다.");

  }

  @Test
  @Order(9)
  @DisplayName("joinPost 취소 테스트")
  void unJoinPostTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(2L).orElse(null);

    //when
    String joinResponse = postService.joinPost(postId, user);

    //then
    assertEquals(joinResponse, "게시물에 참여를 취소합니다.");

  }

  @Test
  @Order(10)
  @DisplayName("joinPost 취소 실패 테스트(본인)")
  void unJoinMyPostTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(1L).orElse(null);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      postService.joinPost(postId, user);
    });

    //then
    assertEquals("자신의 게시물에는 반드시 참여해야 합니다.", exception.getMessage());

  }

  @Test
  @Order(11)
  @DisplayName("updatePost 테스트")
  void updatePostTest(){
    //given
    Long postId = this.createdPost.getId();
    String newTitle = "new title";
    String newContent = "new content";
    String newLocation = "HONGDAE";
    Integer newMaxCount = 1;
    PostRequest postRequest = new PostRequest(newTitle, newContent, newLocation, newMaxCount);
    user = userRepository.findById(1L).orElse(null);

    //when
    PostResponse postResponse = postService.updatePost(postId, postRequest, user);

    //then
    assertNotNull(postResponse.getId());
    assertEquals(newTitle, postResponse.getTitle());
    assertEquals(newContent, postResponse.getContent());
    assertEquals(newLocation, postResponse.getLocation());
    assertEquals(newMaxCount, postResponse.getMaxCount());
    createdPost = postResponse;
  }

  @Test
  @Order(12)
  @DisplayName("joinPost 테스트(정원 초과)")
  void JoinPostFailTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(2L).orElse(null);

    //when
    String joinResponse = postService.joinPost(postId, user);

    //then
    assertEquals(joinResponse, "모집인원이 가득 찼습니다.");

  }

  @Test
  @Order(13)
  @DisplayName("deletePost 실패 테스트")
  void deletePostFailTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(2L).orElse(null);

//when
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      postService.deletePost(postId, user);
    });

    //then
    assertEquals("해당 게시글을 삭제할 권한이 없습니다.", exception.getMessage());
  }

  @Test
  @Order(14)
  @DisplayName("deletePost 테스트")
  void deletePostTest(){
    //given
    Long postId = this.createdPost.getId();
    user = userRepository.findById(1L).orElse(null);

    //when
    String deleteResponse = postService.deletePost(postId, user);

    //then
    assertEquals(deleteResponse, "삭제되었습니다.");
  }

}
