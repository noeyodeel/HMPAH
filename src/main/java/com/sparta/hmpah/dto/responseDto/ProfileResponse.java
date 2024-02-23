package com.sparta.hmpah.dto.responseDto;

import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.entity.UserGenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private String username;
    private String nickname;
    private String profile;
    private Integer age;
    private UserGenderEnum gender;

    public ProfileResponse(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
        this.gender = user.getGender();
        this.age = user.getAge();
    }
}
