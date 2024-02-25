package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.responseDto.FollowerResponse;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.FollowerService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/followers")
public class FollowerController {
    private FollowerService followerService;

    @GetMapping
    public List<FollowerResponse> showFollowers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followerService.showFollowers(userDetails.getUser());
    }

    @GetMapping("/{followerUsername}")
    public InfoResponse showFollower(@PathVariable("followerUsername") String followerUsername) {
        return followerService.showFollowerInfo(followerUsername);
    }

    @DeleteMapping("/{followerUsername}")
    public FollowerResponse deleteFollower(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("followerUsername") String followerUsername) {
        return followerService.deleteFollower(userDetails.getUser(), followerUsername);
    }

}
