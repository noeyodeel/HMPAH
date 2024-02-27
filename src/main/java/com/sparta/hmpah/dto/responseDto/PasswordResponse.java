package com.sparta.hmpah.dto.responseDto;

import lombok.Getter;

@Getter
public class PasswordResponse {
    private String msg;

    public PasswordResponse(String msg) {
        this.msg = msg;
    }
}
