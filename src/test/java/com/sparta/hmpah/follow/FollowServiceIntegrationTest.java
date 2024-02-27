package com.sparta.hmpah.follow;

import com.sparta.hmpah.dto.responseDto.FollowerResponse;
import com.sparta.hmpah.dto.responseDto.FollowingResponse;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.UserRepository;
import com.sparta.hmpah.service.FollowerService;
import com.sparta.hmpah.service.FollowingService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다. // 절차지향처럼 가능
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowServiceIntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowingService followingService;
    @Autowired
    FollowerService followerService;

    User jinchan;
    User follower;
    @BeforeAll
    void before(){
        jinchan = userRepository.findById(1L).orElseThrow(); //nickname : 정진찬, username : username1
        follower = userRepository.findById(2L).orElseThrow();//nickname : 정진찬1, username : username2
    }
    @AfterAll
    void after(){
        followingService.deleteFollowing(jinchan, follower.getId());
    }

    @Test
    @DisplayName("jinchan이 follower의 팔로잉을 끊음")
    void following(){
        //given
        followingService.following(jinchan, follower.getId());
        followingService.following(follower, jinchan.getId());
        //when
        followerService.deleteFollower(jinchan, follower.getId());
        //then
        List<FollowingResponse> followingResponses = followingService.showFollowings(follower);
        List<FollowerResponse> followerResponses = followerService.showFollowers(follower);
        assertEquals(0,followingResponses.size());
        assertEquals(1,followerResponses.size());
        //종료 후 다 제거
        followingService.deleteFollowing(jinchan, follower.getId());
    }
}
