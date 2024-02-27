package com.sparta.hmpah.follow;

import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    FollowRepository followRepository;
    @Mock
    PostRepository postRepository;
    @Test
    @DisplayName("팔로잉을 하면 팔로잉")
    void showFollowings(){
        //given

        //when

        //then

    }
    @Test
    @DisplayName("팔로잉을 하면 팔로워에 표시 된다.")
    void showFollowers(){
        //given

        //when

        //then

    }

    @Test
    @DisplayName("유저가 팔로워를 삭제하면 팔로워는 팔로잉 되어 있는 유저도 삭제된다.")
    void deleteFollower(){
        //given

        //when

        //then

    }

}
