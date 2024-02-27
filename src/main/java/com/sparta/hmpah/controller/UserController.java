package com.sparta.hmpah.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.hmpah.dto.requestDto.LoginInfoRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User", description = "User API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @Operation(summary = "회원가입", description = "일반유저 회원가입")
    @PostMapping("/signup")
    public String signup(@Valid SignupRequest requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/users/signup";
        }
        userService.signup(requestDto);
        return "redirect:/users/login-page";
    }


    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response,
        HttpServletRequest request)
        throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/users/additional-info";
    }

    @GetMapping("/additional-info")
    public String additionalInfoPage(Model model) {
        model.addAttribute("additionalInfoDto", new LoginInfoRequest());

        return "additional-info";
    }

    @Operation(summary = "회원가입", description = "카톡유저 회원가입")
    @PostMapping("/additional-info")
    public String submitAdditionalInfo(@RequestBody LoginInfoRequest loginInfoRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long userId = userDetails.getUser().getKakaoId();
            userService.updateKakaoUserNickname(userId, loginInfoRequest);

            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/users/login-page";
        }
    }
}
