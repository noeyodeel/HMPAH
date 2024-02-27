package com.sparta.hmpah.dto.requestDto;

import com.sparta.hmpah.entity.UserGenderEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoRequest {
    private String nickname;
    private String profile;
    private String gender;
    private Integer age;

}
