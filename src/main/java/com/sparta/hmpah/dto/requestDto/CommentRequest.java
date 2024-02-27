package com.sparta.hmpah.dto.requestDto;

import lombok.Getter;

@Getter
public class CommentRequest {

  private Long postId;
  private String content;
  private Long parentId;
}
