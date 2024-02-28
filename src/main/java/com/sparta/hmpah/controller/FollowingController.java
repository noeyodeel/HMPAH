package com.sparta.hmpah.controller;

import com.sparta.hmpah.dto.responseDto.FollowingResponse;
import com.sparta.hmpah.dto.responseDto.InfoResponse;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.FollowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Followings", description = "Followings API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/followings")
public class FollowingController {
    private final FollowingService followingService;

    @GetMapping
    @Operation(summary = "팔로잉 목록 조회(자신)", description = "자신의 팔로잉 목록을 조회한다.")
    public List<FollowingResponse> showFollowings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followingService.showFollowings(userDetails.getUser());
    }
    @GetMapping("/{followingId}")
    @Operation(summary = "팔로잉 목록 조회(팔로잉 ID)", description = "팔로잉 ID를 통해 사용자의 팔로잉 목록을 조회한다.")
    public List<FollowingResponse> showFollowings(@PathVariable("followingId") Long followingId){
        return followingService.showFollowings(followingId);
    }

    @GetMapping("/{followingId}/profiles")
    @Operation(summary = "팔로잉 정보 조회(팔로잉 ID)", description = "팔로잉 ID를 통해 팔로잉의 정보를 조회한다.")
    public InfoResponse showFollowingInfo(@PathVariable("followingId") Long followingId) {
        return followingService.showFollowingInfo(followingId);
    }


    @PostMapping("/{followingId}")
    @Operation(summary = "팔로잉 (팔로잉 ID)", description = "팔로잉 ID를 통해 사용자를 팔로잉 한다.")
    public FollowingResponse following(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable("followingId") Long followingId) {
        return followingService.following(userDetails.getUser(), followingId);
    }

    @DeleteMapping("/{followingId}")
    @Operation(summary = "팔로잉 삭제(팔로잉 ID)", description = "팔로잉 ID를 통해 팔로잉을 삭제한다.")
    public FollowingResponse deleteFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable("followingId") Long followingId) {
        return followingService.deleteFollowing(userDetails.getUser(), followingId);
    }

}
