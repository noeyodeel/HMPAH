package com.sparta.hmpah.controller;

import static javax.security.auth.callback.ConfirmationCallback.OK;

import com.sparta.hmpah.dto.requestDto.SignupRequest;
import com.sparta.hmpah.dto.responseDto.SignupResponse;
import com.sparta.hmpah.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
        @Valid @RequestBody SignupRequest requestDto) {

        userService.signup(requestDto);
        String message = requestDto.getUsername() + "님 회원가입이 완료되었습니다.";

        return ResponseEntity.ok().body(new SignupResponse(message, OK));

    }
}
