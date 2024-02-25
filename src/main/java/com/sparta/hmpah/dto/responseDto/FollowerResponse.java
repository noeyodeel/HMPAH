package com.sparta.hmpah.dto.responseDto;

import lombok.Getter;

@Getter
public class FollowerResponse {
    private String followerUsername;
    private String followerNickname;

    public FollowerResponse(String followerUsername, String followerNickname) {
        this.followerUsername = followerUsername;
        this.followerNickname = followerNickname;
    }
}
