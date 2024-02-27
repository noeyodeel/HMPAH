package com.sparta.hmpah.post;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.entity.LocationEnum;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.entity.UserRoleEnum;
import com.sparta.hmpah.repository.UserRepository;
import com.sparta.hmpah.service.PostService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
//    user = new User("tkdduq118","knight314!","상엽", UserRoleEnum.USER);
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
  }

}
