package com.sparta.hmpah.dto.responseDto;

import lombok.Getter;

@Getter
public class FollowingResponse {
    private String followingUsername;
    private String followingNickname;

    public FollowingResponse(String followingUsername, String followingNickname) {
        this.followingUsername = followingUsername;
        this.followingNickname = followingNickname;
    }
}
