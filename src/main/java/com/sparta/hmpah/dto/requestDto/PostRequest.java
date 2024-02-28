package com.sparta.hmpah.dto.requestDto;

import com.sparta.hmpah.entity.LocationEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String location;
    private Integer maxcount;
}
