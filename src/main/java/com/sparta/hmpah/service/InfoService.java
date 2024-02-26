package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.PasswordRequest;
import com.sparta.hmpah.dto.requestDto.InfoRequest;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.entity.UserGenderEnum;
import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    public InfoResponse showProfile(User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NullPointerException("존재 하지 않는 유저입니다."));
        //follower
        int followerCount = followRepository.findByFollowing(user).size();
        //following
        int followingCount = followRepository.findByFollower(user).size();
        //post
        List<Post> postsByUser = postRepository.findAllByUser(user);
        return new InfoResponse(findUser.getUsername(),
                findUser.getNickname(),
                findUser.getProfile(),
                findUser.getGender().getValue(),
                findUser.getAge(),
                followerCount,
                followingCount,
                postsByUser
                );
    }
    @Transactional
    public InfoResponse updateProfile(User user, InfoRequest profileRequest){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NullPointerException("존재 하지 않는 유저입니다."));
        //전략 -> 입력값이 없으면 이전 데이터 그대로 (nickname)
        if(profileRequest.getNickname().isEmpty()) profileRequest.setNickname(findUser.getNickname());

        findUser.updateInfo(profileRequest.getNickname(), profileRequest.getProfile(), UserGenderEnum.valueOf(profileRequest.getGender()), profileRequest.getAge());
        //follower
        int followerCount = followRepository.findByFollowing(user).size();
        //following
        int followingCount = followRepository.findByFollower(user).size();
        //post
        List<Post> postsByUser = postRepository.findAllByUser(user);
        return new InfoResponse(findUser.getUsername(),
                findUser.getNickname(),
                findUser.getProfile(),
                findUser.getGender().getValue(),
                findUser.getAge(),
                followerCount,
                followingCount,
                postsByUser
        );
    }
}
