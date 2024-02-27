package com.sparta.hmpah.dto.responseDto;

import com.sparta.hmpah.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

  private Long id;
  private String postTitle;
  private String content;
  private String nickname;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private Integer likescnt;
  private Long parentId;
  private int position;

  public CommentResponse(Comment comment) {
    this.id = comment.getId();
    this.postTitle = comment.getPost().getTitle();
    this.content = comment.getContent();
    this.nickname = comment.getUser().getNickname();
    this.createdAt = comment.getCreatedAt();
    this.modifiedAt = comment.getModifiedAt();
    this.likescnt = comment.getCommentLikes().size();
    this.parentId = comment.getParentId();
    this.position = comment.getPosition();
  }
}
