package com.sparta.hmpah.dto.requestDto;

import com.sparta.hmpah.entity.UserGenderEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseLoginRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\d]{2,10}$", message = "닉네임은 2~10자로 구성되어야 합니다.")
    private String nickname;

    private String profile;
    private UserGenderEnum gender;
    private Integer age;
    private boolean admin = false;
    private String adminToken = "";
}
