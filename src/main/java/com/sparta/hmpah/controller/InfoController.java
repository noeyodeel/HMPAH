package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.InfoRequest;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.InfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class InfoController {
    private final InfoService profileService;
    @GetMapping
    public InfoResponse showProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return profileService.showProfile(userDetails.getUser());
    }

    @PutMapping
    public InfoResponse updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid InfoRequest profileRequest) {
        return profileService.updateProfile(userDetails.getUser(), profileRequest);
    }
//    @PutMapping("/password")

}
