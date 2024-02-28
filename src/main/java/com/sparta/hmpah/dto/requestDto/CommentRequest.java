package com.sparta.hmpah.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRequest {

  private Long postId;
  private String content;
  private Long parentId;
}
