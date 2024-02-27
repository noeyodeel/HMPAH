package com.sparta.hmpah.entity;

import com.sparta.hmpah.dto.requestDto.CommentRequest;
import com.sparta.hmpah.security.UserDetailsImpl;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "position")
  private int position;

  @OneToMany
  @JoinColumn(name = "comment_id")
  private List<CommentLike> commentLikes;

  public Comment(CommentRequest requestDto, User userDetails, Post post, int position) {
    this.content = requestDto.getContent();
    this.user = userDetails;
    this.post = post;
    this.commentLikes = new ArrayList<CommentLike>();
    this.parentId = requestDto.getParentId();
    this.position = position;
  }

  public void update(CommentRequest requestDto) {
    this.content = requestDto.getContent();
  }
}
