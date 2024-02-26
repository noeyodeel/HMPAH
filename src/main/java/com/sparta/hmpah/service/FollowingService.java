package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.responseDto.FollowingResponse;
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
public class FollowingService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    @Transactional
    public List<FollowingResponse> showFollowings(User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        //following들 찾기
        List<Follow> followings = followRepository.findByFollower(findUser);
        List<FollowingResponse> followingResponses = new ArrayList<>();

        for (Follow following : followings) {
            followingResponses.add(new FollowingResponse(following.getFollowing().getUsername(), following.getFollowing().getNickname()));
        }

        return followingResponses;
    }

    public InfoResponse showFollowingInfo(Long followingId) {
        User following = userRepository.findById(followingId).orElseThrow();
        //follower
        int followerCount = followRepository.findByFollowing(following).size();
        //following
        int followingCount = followRepository.findByFollower(following).size();
        //post
        List<Post> postsByFollowing = postRepository.findAllByUser(following);
        return new InfoResponse(following.getUsername(),
                following.getNickname(),
                following.getProfile(),
                following.getGender().getValue(),
                following.getAge(),
                followerCount,
                followingCount,
                postsByFollowing
        );
    }

    @Transactional
    public FollowingResponse deleteFollowing(User user,Long followingId){
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        User following = userRepository.findById(followingId).orElseThrow();

        followRepository.deleteByFollowerAndFollowing(findUser,following);
        return new FollowingResponse(following.getUsername(), following.getNickname());
    }

    @Transactional
    public FollowingResponse following(User user, Long followingId) {
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        User following = userRepository.findById(followingId).orElseThrow();

        Follow save = followRepository.save(new Follow(findUser, following));

        return new FollowingResponse(save.getFollowing().getUsername(), save.getFollowing().getNickname());
    }
}
