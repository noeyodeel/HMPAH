package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.responseDto.FollowerResponse;
import com.sparta.hmpah.dto.responseDto.FollowingResponse;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.FollowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Followers", description = "Followers API")
@RequestMapping("/followers")
public class FollowerController {
    private final FollowerService followerService;

    @GetMapping
    @Operation(summary = "팔로워 목록 조회(자신)", description = "자신의 팔로워 목록을 조회한다.")
    public List<FollowerResponse> showFollowers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followerService.showFollowers(userDetails.getUser());
    }
    @GetMapping("/{followerId}")
    @Operation(summary = "팔로워 목록 조회(팔로워 ID)", description = "팔로워 ID를 통해 사용자의 팔로워 목록을 조회한다.")
    public List<FollowerResponse> showFollowings(@PathVariable("followerId") Long followerId){
        return followerService.showFollowers(followerId);
    }

    @GetMapping("/{followerId}/profiles")
    @Operation(summary = "팔로워 정보 조회(팔로워 ID)", description = "팔로워 ID를 통해 팔로워의 정보를 조회한다.")
    public InfoResponse showFollowerInfo(@PathVariable("followerId") Long followerId) {
        return followerService.showFollowerInfo(followerId);
    }

    @DeleteMapping("/{followerId}")
    @Operation(summary = "팔로워 삭제(팔로워 ID)", description = "팔로워 ID를 통해 팔로워를 삭제한다.")
    public FollowerResponse deleteFollower(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("followerId") Long followerId) {
        return followerService.deleteFollower(userDetails.getUser(), followerId);
    }

}
