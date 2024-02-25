package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.PasswordRequest;
import com.sparta.hmpah.dto.requestDto.InfoRequest;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoService {
    private final UserRepository userRepository;
    public InfoResponse showProfile(User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NullPointerException("존재 하지 않는 유저입니다."));
        return new InfoResponse(findUser.getUsername(), findUser.getNickname(),
                findUser.getProfile(), findUser.getGender(), findUser.getAge());
    }
    @Transactional
    public InfoResponse updateProfile(User user, InfoRequest profileRequest){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NullPointerException("존재 하지 않는 유저입니다."));
        //전략 -> 입력값이 없으면 이전 데이터 그대로 (nickname)
        if(profileRequest.getNickname().isEmpty()) profileRequest.setNickname(findUser.getNickname());

        findUser.updateInfo(profileRequest.getNickname(), profileRequest.getProfile(),profileRequest.getGender(), profileRequest.getAge());
        return new InfoResponse(findUser.getUsername(), findUser.getNickname(),
                findUser.getProfile(), findUser.getGender(), findUser.getAge());
    }

    @Transactional
    public String updatePassword(User user, PasswordRequest passwordRequest) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new NullPointerException("존재 하지 않는 유저입니다."));


        return "변경되었습니다.";
    }
}
