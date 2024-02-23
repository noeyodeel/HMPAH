package com.sparta.hmpah.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignResponse {
    private String msg;
    private int statusCode;
}
