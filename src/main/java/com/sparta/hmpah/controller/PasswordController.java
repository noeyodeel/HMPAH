package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.PasswordRequest;
import com.sparta.hmpah.dto.responseDto.PasswordResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passwords")
public class PasswordController {

    private final PasswordService passwordService;
    @PostMapping
    public PasswordResponse updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequest passwordRequest) {
        return passwordService.updatePassword(userDetails.getUser(), passwordRequest);
    }
}
