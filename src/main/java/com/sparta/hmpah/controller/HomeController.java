package com.sparta.hmpah.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.hmpah.dto.requestDto.SignupRequest;
import com.sparta.hmpah.jwt.JwtUtil;
import com.sparta.hmpah.service.KakaoService;
import com.sparta.hmpah.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Slf4j
@RequiredArgsConstructor
@RequestMapping("")
@Controller
public class HomeController {

    private final UserService userService;
    private final KakaoService kakaoService;
    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }
//    @PostMapping("/user/signup")
//    public String signup(@Valid SignupRequest requestDto, BindingResult bindingResult) {
//        // Validation 예외처리
//        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//        if(fieldErrors.size() > 0) {
//            for (FieldError fieldError : bindingResult.getFieldErrors()) {
//                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
//            }
//            return "redirect:/user/signup";
//        }
//
//        userService.signup(requestDto);
//
//        return "redirect:/user/login-page";
//    }
    @GetMapping("api/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response)
        throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/user/kakao/login";
    }

}