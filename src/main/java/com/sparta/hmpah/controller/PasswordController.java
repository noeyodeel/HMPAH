package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.PasswordRequest;
import com.sparta.hmpah.dto.responseDto.PasswordResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Passwords", description = "Passwords API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/passwords")
public class PasswordController {

    private final PasswordService passwordService;
    @PostMapping
    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경한다.")
    public PasswordResponse updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequest passwordRequest) {
        return passwordService.updatePassword(userDetails.getUser(), passwordRequest);
    }
}
