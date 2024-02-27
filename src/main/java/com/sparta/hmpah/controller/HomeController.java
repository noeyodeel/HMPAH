package com.sparta.hmpah.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.hmpah.dto.requestDto.AdditionalInfoRequest;
import com.sparta.hmpah.dto.requestDto.SignupRequest;
import com.sparta.hmpah.jwt.JwtUtil;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.KakaoService;
import com.sparta.hmpah.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.*;

@Tag(name = "Home", description = "Home API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("")
@Controller
public class HomeController {

    private final UserService userService;
    private final KakaoService kakaoService;


    @Operation(summary = "메인", description = "메인페이지")
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

//    @GetMapping("/user/signup")
//    public String signupPage() {
//        return "signup";
//    }
//
//    @Operation(summary = "회원가입", description = "일반유저 회원가입")
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
//        userService.signup(requestDto);
//        return "redirect:/user/login-page";
//    }

    @GetMapping("api/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response,
        HttpServletRequest request)
        throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/user/additional-info";

    }

    @GetMapping("/user/additional-info")
    public String additionalInfoPage(Model model) {
        model.addAttribute("additionalInfoDto", new AdditionalInfoRequest());

        return "additional-info";
    }
    @Operation(summary = "회원가입", description = "카톡유저 회원가입")
    @PostMapping("/user/additional-info")
    public String submitAdditionalInfo(@RequestBody AdditionalInfoRequest additionalInfoRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long userId = userDetails.getUser().getKakaoId();

            userService.updateKakaoUserNickname(userId, additionalInfoRequest);

            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/user/login-page";
        }
    }
}

