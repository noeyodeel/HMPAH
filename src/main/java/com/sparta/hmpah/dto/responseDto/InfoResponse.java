package com.sparta.hmpah.dto.responseDto;

import com.sparta.hmpah.entity.UserGenderEnum;
import lombok.Getter;

@Getter
public class InfoResponse {
    private String username;
    private String nickname;
    private String profile;
    private Integer age;
    private UserGenderEnum gender;

    public InfoResponse(String username, String nickname, String profile, UserGenderEnum gender, Integer age) {
        this.username = username;
        this.nickname = nickname;
        this.profile = profile;
        this.gender = gender;
        this.age = age;
    }
}
