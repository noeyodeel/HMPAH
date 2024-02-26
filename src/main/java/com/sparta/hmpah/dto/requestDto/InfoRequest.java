package com.sparta.hmpah.dto.requestDto;

import com.sparta.hmpah.entity.UserGenderEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoRequest {
    @NotBlank(message = "필수")
    private String nickname;
    private String profile;
    @NotBlank(message = "필수")
    private UserGenderEnum gender;
    private Integer age;

}
