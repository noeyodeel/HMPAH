package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.responseDto.FollowingResponse;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.entity.Follow;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowingService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public List<FollowingResponse> showFollowings(User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        //following들 찾기
        List<Follow> followings = followRepository.findByFollower(findUser);
        List<FollowingResponse> followingResponses = new ArrayList<>();

        for (Follow following : followings) {
            followingResponses.add(new FollowingResponse(following.getFollower().getUsername(), following.getFollower().getNickname()));
        }

        return followingResponses;
    }

    public InfoResponse showFollowingInfo(String followingUsername) {
        User following = userRepository.findByUsername(followingUsername).orElseThrow();
        return new InfoResponse(following.getUsername(), following.getNickname(), following.getProfile(), following.getGender(), following.getAge());
    }

    @Transactional
    public FollowingResponse deleteFollowing(User user,String followingUsername){
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        User following = userRepository.findByUsername(followingUsername).orElseThrow();

        followRepository.deleteByFollowerAndFollowing(findUser,following);
        return new FollowingResponse(following.getUsername(), following.getNickname());
    }

    @Transactional
    public FollowingResponse following(User user, String followingUsername) {
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        User following = userRepository.findByUsername(followingUsername).orElseThrow();

        Follow save = followRepository.save(new Follow(findUser, following));

        return new FollowingResponse(save.getFollowing().getUsername(), save.getFollowing().getNickname());
    }
}
