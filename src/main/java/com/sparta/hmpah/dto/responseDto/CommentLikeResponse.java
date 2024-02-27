package com.sparta.hmpah.dto.responseDto;

import com.sparta.hmpah.entity.CommentLike;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentLikeResponse {
  Long commentId;
  Long userId;

  public CommentLikeResponse(CommentLike commentLike){
    this.commentId = commentLike.getComment().getId();
    this.userId = commentLike.getUser().getId();
  }

}
