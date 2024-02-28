package com.sparta.hmpah.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.hmpah.dto.requestDto.CommentLikeRequest;
import com.sparta.hmpah.dto.requestDto.CommentRequest;
import com.sparta.hmpah.dto.responseDto.CommentResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.CommentRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import com.sparta.hmpah.service.CommentService;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
public class CommentIntegrationTest {

  @Autowired
  CommentService commentService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CommentRepository repository;

  @Autowired
  PostRepository postRepository;

  User user;//작성자

  User user2;//비작성자

  Long firstCreateCommentId;//성성한 댓글 id

  List<CommentResponse> beforeList;//테스트전 DB 상태

  Post post;


  @BeforeEach
  void setUp() {
    user = userRepository.findById(1L).orElseThrow();//작성자
    user2 = userRepository.findById(2L).orElseThrow();//비작성자
    beforeList = commentService.getComments(1L);//테스트전 DB 상태
    post = postRepository.findById(1L).orElseThrow();//작성대상 포스트
  }

  @DisplayName("Comment(댓글)-creat 테스트")
  @Test
  @Order(1)
  void commentCreate(){

    //given
    Long postId = 1L;
    String content = "댓글 생성";
    CommentRequest request = new CommentRequest();
    request.setContent(content);
    request.setPostId(postId);

    //when
    CommentResponse response = commentService.createComment(request,user);
    firstCreateCommentId = response.getId();

    //then
    assertNotNull(response.getId());
    assertEquals(post.getTitle(),response.getPostTitle());
    assertEquals(content,response.getContent());
    assertEquals(user.getNickname(),response.getNickname());
    assertEquals(0,response.getLikescnt());
    assertNull(response.getParentId());
    assertEquals(0,response.getPosition());
  }

  @DisplayName("Comment(대댓글)-creat 테스트")
  @Test
  @Order(2)
  void commentChildCreate(){

    //given
    Long postId = 1L;
    String content = "댓글 생성";
    CommentRequest request = new CommentRequest();
    request.setContent(content);
    request.setPostId(postId);
    request.setParentId(firstCreateCommentId);

    //when
    CommentResponse response = commentService.createComment(request,user);
    CommentResponse response2 = commentService.createComment(request,user);

    //then
    assertNotNull(response.getId());
    assertEquals(post.getTitle(),response.getPostTitle());
    assertEquals(content,response.getContent());
    assertEquals(user.getNickname(),response.getNickname());
    assertEquals(0,response.getLikescnt());
    assertEquals(firstCreateCommentId,response.getParentId());
    assertEquals(1,response.getPosition());

    assertNotNull(response2.getId());
    assertEquals(post.getTitle(),response2.getPostTitle());
    assertEquals(content,response2.getContent());
    assertEquals(user.getNickname(),response2.getNickname());
    assertEquals(0,response2.getLikescnt());
    assertEquals(firstCreateCommentId,response2.getParentId());
    assertEquals(2,response2.getPosition());


    System.out.println("response = " + response);
    System.out.println("response2 = " + response2);
  }

  @DisplayName("Comment-Update 테스트")
  @Test
  @Order(3)
  void commentUpdate(){

    //given
    String content = "댓글 수정";
    CommentRequest request = new CommentRequest();
    request.setContent(content);

    //when
    CommentResponse response = commentService.updateComment(firstCreateCommentId, request, user);

    //then
    assertEquals(content,response.getContent());
    System.out.println("response = " + response);
  }

  @DisplayName("Comment-finByCommentId")
  @Test
  @Order(4)
  void findByCommentId(){

    //given
    String content = "댓글 수정";

    //when
    Comment comment = commentService.findyComment(firstCreateCommentId);

    //then

    assertNotNull(comment.getId());
    assertEquals(post.getTitle(),comment.getPost().getTitle());
    assertEquals(content,comment.getContent());
    assertEquals(user.getNickname(),comment.getUser().getNickname());
    assertNull(comment.getParentId());
    assertEquals(0,comment.getPosition());

    System.out.println("comment = " + comment);

  }


  @DisplayName("Comment-작성자 검증")
  @Test
  @Order(5)
  void validateUsername(){

    //when
    Comment comment = commentService.findyComment(firstCreateCommentId);

    //then
    boolean result1 = commentService.validateUsername(comment,user);
    boolean result2 = commentService.validateUsername(comment,user2);

    assertTrue(result1);
    assertFalse(result2);
  }

  @DisplayName("CommentLike-create 테스트")
  @Test
  @Order(6)
  void createCommentLike(){
    //given
    CommentLikeRequest request = new CommentLikeRequest();
    request.setCommentId(firstCreateCommentId);

    //when
    Long result1 = commentService.createCommentLike(request,user);
    Long result2 = commentService.createCommentLike(request,user2);

    //then
    assertEquals(0,result1);
    assertEquals(1,result2);
    System.out.println(result1);
    System.out.println(result2);
  }

  @DisplayName("CommentLike-delete 테스트")
  @Test
  @Order(7)
  void deleteCommentLike(){

    //when
    Long result1 = commentService.deleteCommentLike(firstCreateCommentId,user);
    Long result2 = commentService.deleteCommentLike(firstCreateCommentId,user2);

    //then
    assertEquals(1,result1);
    assertEquals(0,result2);
    System.out.println("result1 = " + result1);
    System.out.println("result2 = " + result2);

  }



  @DisplayName("Comment-delete 테스트")
  @Test
  @Order(8)
  void commentDelete(){

    //when
    List<CommentResponse> result = commentService.deleteComment(firstCreateCommentId,user);

    //then
    for(int i = 0; i < result.size(); i++){
      assertEquals(beforeList.get(i).getId(),result.get(i).getId());
      assertEquals(beforeList.get(i).getPostTitle(),result.get(i).getPostTitle());
      assertEquals(beforeList.get(i).getContent(),result.get(i).getContent());
      assertEquals(beforeList.get(i).getNickname(),result.get(i).getNickname());
      assertEquals(beforeList.get(i).getLikescnt(),result.get(i).getLikescnt());
      assertEquals(beforeList.get(i).getParentId(),result.get(i).getParentId());
      assertEquals(beforeList.get(i).getPosition(),result.get(i).getPosition());
    }

  }

}
