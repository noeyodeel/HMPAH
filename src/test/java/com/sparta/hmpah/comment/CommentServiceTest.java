package com.sparta.hmpah.comment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

import com.sparta.hmpah.dto.requestDto.CommentRequest;
import com.sparta.hmpah.dto.responseDto.CommentResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.CommentLikeRepository;
import com.sparta.hmpah.repository.CommentRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.service.CommentService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

  @Mock
  PostRepository postRepository;

  @Mock
  CommentRepository commentRepository;

  @Mock
  CommentLikeRepository commentLikeRepository;

  @Test
  @DisplayName("updateComment")
  void test(){
    CommentService commentService = new CommentService(postRepository,commentRepository,commentLikeRepository);
    CommentRequest requestDto = new CommentRequest();
    String content = "내용수정";
    requestDto.setContent(content);
    User user = new User();
    Post post = new Post();
    user.setId(2L);
    post.setUser(user);
    Comment comment = new Comment(requestDto, user, post, 0);
    given(commentRepository.findById(2L)).willReturn(Optional.of(comment));
    CommentResponse result = commentService.updateComment(2L, requestDto ,user);
    assertEquals(content, result.getContent());
  }
}
