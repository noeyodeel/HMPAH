package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.AdditionalInfoRequest;
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

    public void updateKakaoUserNickname(Long id, AdditionalInfoRequest additionalInfo) {
        User kakaoUser = userRepository.findByKakaoId(id).orElse(null);
        String nickname = additionalInfo.getNickname();
        String profile = additionalInfo.getProfile();
        Integer age = additionalInfo.getAge();
        UserGenderEnum gender = additionalInfo.getGender();
        UserRoleEnum role = UserRoleEnum.USER;

        if (kakaoUser != null && kakaoUser.getKakaoId() != null) {
            kakaoUser.nicknameUpdate(nickname);

            if (age != null) {
                kakaoUser.setAge(age);
            }
            if (gender != null) {
                kakaoUser.setGender(gender);
            }
            if (profile != null && !profile.isEmpty()) {
                kakaoUser.setProfile(profile);
            }

            if (additionalInfo.isAdmin()) {
                if (!ADMIN_TOKEN.equals(additionalInfo.getAdminToken())) {
                    throw new IllegalArgumentException("관리자 인증키가 틀려 등록이 불가능합니다.");
                }
                role = UserRoleEnum.ADMIN;
                kakaoUser.setRole(role);
            }
            kakaoUser.setRole(role);
            userRepository.save(kakaoUser);
        }
    }

    private UserRoleEnum validAdmin(SignupRequest requestDto, UserRoleEnum role) {
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 인증키가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
            return role;
        }
        return role;
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

}
