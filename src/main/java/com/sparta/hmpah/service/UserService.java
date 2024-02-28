package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.LoginInfoRequest;
import com.sparta.hmpah.dto.requestDto.SignupRequest;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.entity.UserGenderEnum;
import com.sparta.hmpah.entity.UserRoleEnum;
import com.sparta.hmpah.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequest requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();
        String email = requestDto.getEmail();
        String profile = requestDto.getProfile();
        Integer age = requestDto.getAge();
        UserGenderEnum gender = requestDto.getGender();
        UserRoleEnum role = UserRoleEnum.USER;

        validUser(username);
        validEmail(email);
        validAdmin(requestDto, role);

        User user = new User(username, password, nickname, email, role);
        if (age != null) {
            user.setAge(age);
        }
        if (gender != null) {
            user.setGender(gender);
        }
        if (profile != null && !profile.isEmpty()) {
            user.setProfile(profile);
        }

        userRepository.save(user);
    }

    public void updateKakaoUserNickname(Long id, LoginInfoRequest additionalInfo) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        if (additionalInfo == null) {
            throw new IllegalArgumentException("AdditionalInfo가 제공되지 않았습니다.");
        }

        try {
            User kakaoUser = userRepository.findByKakaoId(id).orElse(null);

            Optional.ofNullable(additionalInfo.getNickname()).ifPresent(kakaoUser::nicknameUpdate);
            Optional.ofNullable(additionalInfo.getAge()).ifPresent(kakaoUser::setAge);
            Optional.ofNullable(additionalInfo.getGender()).ifPresent(kakaoUser::setGender);
            Optional.ofNullable(additionalInfo.getProfile())
                .filter(profile -> !profile.isEmpty())
                .ifPresent(kakaoUser::setProfile);

            validAdmin(additionalInfo, kakaoUser.getRole());

            userRepository.save(kakaoUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("사용자 정보 업데이트 중 오류가 발생했습니다.");
        }
    }


    private void validEmail(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }
    }

    private void validUser(String username) {
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
    }

    public UserRoleEnum validAdmin(LoginInfoRequest requestDto, UserRoleEnum role) {
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 인증키가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
            return role;
        }
        return role;
    }

}
