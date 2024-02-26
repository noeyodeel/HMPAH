package com.sparta.hmpah.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hmpah.dto.requestDto.AdditionalInfoRequest;
import com.sparta.hmpah.dto.responseDto.KakaoUserInfoDto;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.entity.UserGenderEnum;
import com.sparta.hmpah.entity.UserRoleEnum;
import com.sparta.hmpah.jwt.JwtUtil;
import com.sparta.hmpah.repository.UserRepository;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public String kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        //3.필요시에 회원가입
        User kakoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        //4. JWT 토큰 반환
        String createtoken = jwtUtil.createToken(kakoUser.getUsername(), kakoUser.getRole());
        return createtoken;
    }

    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드 :" + code);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com")
            .path("/oauth/token")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "da69a22183d394b6687ef78454a12c34");
        body.add("redirect_uri", "http://localhost:8080/api/user/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("acessToken :" + accessToken);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com")
            .path("/v2/user/me")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
            .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + email);
        return new KakaoUserInfoDto(id, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(encodedPassword, email,
                    UserRoleEnum.USER, kakaoId);
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }


    public void updateKakaoUserNickname(Long id, AdditionalInfoRequest additionalInfo) {
        User kakaoUser = userRepository.findByKakaoId(id).orElse(null);
        String nickname = additionalInfo.getNickname();
        String profile = additionalInfo.getProfile();
        Integer age = additionalInfo.getAge();
        UserGenderEnum gender = additionalInfo.getGender();
        UserRoleEnum role = UserRoleEnum.USER;

        if (kakaoUser != null && kakaoUser.getKakaoId() != null) {
            // Kakao로 가입한 사용자의 nickname 업데이트
            kakaoUser.nicknameUpdate(nickname);

            // age, gender, profile이 null이 아니면 업데이트
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


}