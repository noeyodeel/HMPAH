package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.responseDto.FollowerResponse;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.entity.Follow;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowerService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;


    public List<FollowerResponse> showFollowers(User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        //follower들 찾기
        List<Follow> followers = followRepository.findByFollowing(findUser);
        List<FollowerResponse> followerResponses = new ArrayList<>();

        for (Follow follower : followers) {
            followerResponses.add(new FollowerResponse(follower.getFollower().getUsername(), follower.getFollower().getNickname()));
        }

        return followerResponses;
    }
    public List<FollowerResponse> showFollowers(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow();
        //follower들 찾기
        List<Follow> followers = followRepository.findByFollowing(findUser);
        List<FollowerResponse> followerResponses = new ArrayList<>();

        for (Follow follower : followers) {
            followerResponses.add(new FollowerResponse(follower.getFollower().getUsername(), follower.getFollower().getNickname()));
        }

        return followerResponses;
    }

    public InfoResponse showFollowerInfo(Long followerId) {
        User follower = userRepository.findById(followerId).orElseThrow();
        //follower
        int followerCount = followRepository.findByFollowing(follower).size();
        //following
        int followingCount = followRepository.findByFollower(follower).size();
        //post
        List<Post> postsByFollower = postRepository.findAllByUser(follower);
        return new InfoResponse(follower.getUsername(),
                follower.getNickname(),
                follower.getProfile(),
                follower.getGender().getValue(),
                follower.getAge(),
                followerCount,
                followingCount,
                postsByFollower
        );
    }

    @Transactional
    public FollowerResponse deleteFollower(User user,Long followerId){
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        User follower = userRepository.findById(followerId).orElseThrow();

        followRepository.deleteByFollowerAndFollowing(follower,findUser);
        return new FollowerResponse(follower.getUsername(), follower.getNickname());
    }
}
