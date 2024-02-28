package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.responseDto.FollowerResponse;
import com.sparta.hmpah.dto.responseDto.FollowingResponse;
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
    private final FollowerService followerService;

    @GetMapping
    public List<FollowerResponse> showFollowers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followerService.showFollowers(userDetails.getUser());
    }
    @GetMapping("/{followerId}")
    public List<FollowerResponse> showFollowings(@PathVariable("followerId") Long followerId){
        return followerService.showFollowers(followerId);
    }

    @GetMapping("/{followerId}/profiles")
    public InfoResponse showFollowerInfo(@PathVariable("followerId") Long followerId) {
        return followerService.showFollowerInfo(followerId);
    }

    @DeleteMapping("/{followerId}")
    public FollowerResponse deleteFollower(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("followerId") Long followerId) {
        return followerService.deleteFollower(userDetails.getUser(), followerId);
    }

}
