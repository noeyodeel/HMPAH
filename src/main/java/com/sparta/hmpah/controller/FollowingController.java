package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.responseDto.FollowingResponse;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.FollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/followings")
public class FollowingController {
    private FollowingService followingService;

    @GetMapping
    public List<FollowingResponse> showFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followingService.showFollowings(userDetails.getUser());
    }

    @GetMapping("/{followingId}")
    public InfoResponse showFollowing(@PathVariable("followingId") Long followingId) {
        return followingService.showFollowingInfo(followingId);
    }

    @PostMapping("/{followingId}")
    public FollowingResponse following(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable("followingId") Long followingId) {
        return followingService.following(userDetails.getUser(), followingId);
    }

    @DeleteMapping("/{followingId}")
    public FollowingResponse deleteFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable("followingId") Long followingId) {
        return followingService.deleteFollowing(userDetails.getUser(), followingId);
    }

}
