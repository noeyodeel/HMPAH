package com.sparta.hmpah.controller;

import static javax.security.auth.callback.ConfirmationCallback.OK;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.hmpah.dto.requestDto.SignupRequest;
import com.sparta.hmpah.dto.responseDto.SignupResponse;
import com.sparta.hmpah.jwt.JwtUtil;
import com.sparta.hmpah.service.KakaoService;
import com.sparta.hmpah.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;



//    @PostMapping("/user/signup")
//    public ResponseEntity<SignupResponse> signup(
//        @Valid @RequestBody SignupRequest requestDto) {
//
//        userService.signup(requestDto);
//        String message = requestDto.getUsername() + "님 회원가입이 완료되었습니다.";
//
//        return ResponseEntity.ok().body(new SignupResponse(message, OK));
//    }


}
