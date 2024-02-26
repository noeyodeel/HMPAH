package com.sparta.hmpah.dto.responseDto;

import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.UserGenderEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class InfoResponse {
    private String username;
    private String nickname;
    private String profile;
    private Integer age;
    private String gender;
    private Integer followerCount;
    private Integer followingCount;
    private List<Post> posts;

    public InfoResponse(String username, String nickname, String profile, String gender,Integer age,Integer followerCount, Integer followingCount,List<Post> posts) {
        this.username = username;
        this.nickname = nickname;
        this.profile = profile;
        this.age = age;
        if(gender == null){
            this.gender = "FEMALE";
        }
        else {
            this.gender = gender;
        }
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.posts = posts;
    }
}
