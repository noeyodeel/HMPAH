package com.sparta.hmpah.entity;

import static com.sparta.hmpah.entity.PostStatusEnum.*;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "posts")
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer maxCount;

    @Column(nullable = false)
    private PostStatusEnum status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private LocationEnum location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Post(PostRequest postRequest, User user) {
        this.title = postRequest.getTitle();
        this.content = postRequest.getContent();
        this.maxCount = postRequest.getMaxcount();
        this.location = LocationEnum.getEnum(postRequest.getLocation());
        this.user = user;
        this.status = RECRUTING;
    }

    public void update(PostRequest postRequest) {
        this.title = postRequest.getTitle();
        this.content = postRequest.getContent();
        this.maxCount = postRequest.getMaxcount();
        this.location = LocationEnum.getEnum(postRequest.getLocation());
    }

    public void updateStatus(Integer currentCount) {
        if(this.maxCount == currentCount)
            this.status = COMPLETED;
        else
            this.status = RECRUTING;
    }
}
