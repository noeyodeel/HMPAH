package com.sparta.hmpah.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

@Getter
@Setter
public class SignupRequest extends LoginInfoRequest {

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d).{4,10}$",
        message = "아이디는 최소 4자 이상, 10자 이하로 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\W)(?=.*\\d).{8,15}$",
        message = "비밀번호는 최소 8자 이상, 15자 이하로 알파벳과 특수문자, 숫자로 구성되어야 합니다.")
    private String password;

    @Email
    @NotBlank
    private String email;


}
