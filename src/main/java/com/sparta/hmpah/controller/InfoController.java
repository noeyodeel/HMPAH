package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.requestDto.InfoRequest;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Info", description = "Info API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class InfoController {
    private final InfoService infoService;
    @GetMapping
    @Operation(summary = "프로필 조회", description = "프로필을 조회한다.")
    public InfoResponse showProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return infoService.showProfile(userDetails.getUser());
    }

    @PutMapping
    @Operation(summary = "프로필 수정", description = "프로필을 수정한다.")
    public InfoResponse updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid InfoRequest profileRequest) {
        return infoService.updateProfile(userDetails.getUser(), profileRequest);
    }

}
