package com.sparta.hmpah.dto.responseDto;

import static com.sparta.hmpah.entity.PostStatusEnum.*;

import com.sparta.hmpah.entity.LocationEnum;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.PostStatusEnum;
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
    private Boolean isMember;

    public PostResponse(Post post, Integer currentCount, Integer likescnt, Boolean isMember) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.location = post.getLocation().getLabel();
        this.nickname = post.getUser().getNickname();
        this.maxCount = post.getMaxCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.currentCount = currentCount;
        this.likescnt = likescnt;
        this.status = post.getStatus().getLabel();
        this.isMember = isMember;
    }


}
