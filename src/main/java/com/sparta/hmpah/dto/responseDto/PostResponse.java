package com.sparta.hmpah.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String location;
    private String nickname;
    private String status;
    private Integer maxCount;
    private Integer currentCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer likescnt;
}
