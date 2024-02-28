package com.sparta.hmpah.follow;

import com.sparta.hmpah.dto.responseDto.FollowerResponse;
import com.sparta.hmpah.dto.responseDto.FollowingResponse;
import com.sparta.hmpah.entity.Follow;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import com.sparta.hmpah.service.FollowerService;
import com.sparta.hmpah.service.FollowingService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    FollowRepository followRepository;
    @Mock
    PostRepository postRepository;

    @Test
    @DisplayName("팔로잉을 하면 팔로잉에 표시")
    void showFollowings(){
        //given
        User follower = new User();
        follower.setId(100L);
        follower.setUsername("jinchan123");
        follower.setNickname("huntingMan");

        List<User> followings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("username" + i);
            user.setNickname("nickname" + i);
            followings.add(user);
        }
        List<Follow> follows = new ArrayList<>();
        //0번이 1,2,3,4번을 팔로잉 하는 상황
        for (int i = 0; i < 5; i++) {
            follows.add(new Follow(follower, followings.get(i)));
        }
        FollowingService followingService = new FollowingService(userRepository, followRepository, postRepository);
        //when
        when(followRepository.findByFollower(any(User.class))).thenReturn(follows);

        given(userRepository.findById(follower.getId())).willReturn(Optional.of(follower));
        List<FollowingResponse> followingResponses = followingService.showFollowings(follower);
        //then
        assertEquals(5,followingResponses.size());
        assertEquals(followings.get(0).getUsername(), followingResponses.get(0).getFollowingUsername());
        assertEquals(followings.get(1).getUsername(), followingResponses.get(1).getFollowingUsername());
        assertEquals(followings.get(2).getUsername(), followingResponses.get(2).getFollowingUsername());
        assertEquals(followings.get(3).getUsername(), followingResponses.get(3).getFollowingUsername());
        assertEquals(followings.get(4).getUsername(), followingResponses.get(4).getFollowingUsername());
    }
    @Test
    @DisplayName("팔로잉을 하면 팔로워에 표시 된다.")
    void showFollowers(){
        //given
        User follower = new User();
        follower.setId(100L);
        follower.setUsername("jinchan123");
        follower.setNickname("huntingMan");

        List<User> followings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("username" + i);
            user.setNickname("nickname" + i);
            followings.add(user);
        }
        List<Follow> follows = new ArrayList<>();
        //0,1,2,3,4번이 100번을 팔로잉 하는 상황
        for (int i = 0; i < 5; i++) {
            follows.add(new Follow(followings.get(i),follower));
        }
        FollowerService followerService = new FollowerService(userRepository, followRepository, postRepository);
        //when
        when(followRepository.findByFollowing(any(User.class))).thenReturn(follows);
        given(userRepository.findById(follower.getId())).willReturn(Optional.of(follower));
        List<FollowerResponse> followerResponses = followerService.showFollowers(follower);
        //then
        assertEquals(5,followerResponses.size());
        assertEquals(followings.get(0).getUsername(),followerResponses.get(0).getFollowerUsername());
        assertEquals(followings.get(1).getUsername(),followerResponses.get(1).getFollowerUsername());
        assertEquals(followings.get(2).getUsername(),followerResponses.get(2).getFollowerUsername());
        assertEquals(followings.get(3).getUsername(),followerResponses.get(3).getFollowerUsername());
        assertEquals(followings.get(4).getUsername(),followerResponses.get(4).getFollowerUsername());
    }

    @Test
    @DisplayName("유저가 팔로워를 삭제")
    void deleteFollower(){
        //given
        User follower = new User();
        follower.setId(100L);
        follower.setUsername("jinchan123");
        follower.setNickname("huntingMan");

        List<User> followings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("username" + i);
            user.setNickname("nickname" + i);
            followings.add(user);
        }
        List<Follow> follows = new ArrayList<>();
        //0,1,2,3,4번이 100번을 팔로잉 하는 상황
        for (int i = 0; i < 5; i++) {
            follows.add(new Follow(followings.get(i),follower));
        }
        FollowerService followerService = new FollowerService(userRepository, followRepository, postRepository);

        given(userRepository.findById(follower.getId())).willReturn(Optional.of(follower));
        given(userRepository.findById(0L)).willReturn(Optional.of(followings.get(0)));
        //when
        FollowerResponse followerResponse = followerService.deleteFollower(follower, 0L);

        //then
        assertEquals(followings.get(0).getUsername(),followerResponse.getFollowerUsername());
    }

}
