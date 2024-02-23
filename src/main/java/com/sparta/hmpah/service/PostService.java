package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.PostLike;
import com.sparta.hmpah.entity.PostMember;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.CommentLikeRepository;
import com.sparta.hmpah.repository.CommentRepository;
import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PostLikeRepository;
import com.sparta.hmpah.repository.PostMemberRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PostLikeRepository postLikeRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final PostMemberRepository postMemberRepository;
  private final FollowRepository followRepository;

  public List<PostResponse> getPostList(User user) {
    List<Post> postList = postRepository.findAll();
    List<PostResponse> postResponseList = new ArrayList<>();

    for (Post post : postList) {
      Optional<PostMember> postMember = Optional.ofNullable(
          postMemberRepository.findByPostAndUser(post, user));
      Integer currentCount = postMemberRepository.findAllByPost(post).size();
      Integer likescnt = postLikeRepository.findAllByPost(post).size();
      post.updateStatus(currentCount);
      postResponseList.add(new PostResponse(post, currentCount, likescnt, postMember.isPresent()));
    }

    return postResponseList;
  }

  public List<PostResponse> getPostListByStatus(String status, User user) {
    List<Post> postList = postRepository.findAllByStatus(status);
    List<PostResponse> postResponseList = new ArrayList<>();

    for (Post post : postList) {
      Optional<PostMember> postMember = Optional.ofNullable(
          postMemberRepository.findByPostAndUser(post, user));
      Integer currentCount = postMemberRepository.findAllByPost(post).size();
      Integer likescnt = postLikeRepository.findAllByPost(post).size();
      post.updateStatus(currentCount);
      postResponseList.add(new PostResponse(post, currentCount, likescnt, postMember.isPresent()));
    }

    return postResponseList;
  }


  public List<PostResponse> getPostListByLocation(String location, User user) {
    List<Post> postList = postRepository.findAllByLocation(location);
    List<PostResponse> postResponseList = new ArrayList<>();

    for (Post post : postList) {
      Optional<PostMember> postMember = Optional.ofNullable(
          postMemberRepository.findByPostAndUser(post, user));
      Integer currentCount = postMemberRepository.findAllByPost(post).size();
      Integer likescnt = postLikeRepository.findAllByPost(post).size();
      post.updateStatus(currentCount);
      postResponseList.add(new PostResponse(post, currentCount, likescnt, postMember.isPresent()));
    }

    return postResponseList;
  }

  public List<PostResponse> getPostListByFollow(User user) {
    List<User> followings = followRepository.findAllByFollower(user);
    List<Post> postList = new ArrayList<>();
    List<PostResponse> postResponseList = new ArrayList<>();

    for (User following : followings) {
      postList.addAll(postRepository.findAllByUser(following));
    }

    for (Post post : postList) {
      Optional<PostMember> postMember = Optional.ofNullable(
          postMemberRepository.findByPostAndUser(post, user));
      Integer currentCount = postMemberRepository.findAllByPost(post).size();
      Integer likescnt = postLikeRepository.findAllByPost(post).size();
      post.updateStatus(currentCount);
      postResponseList.add(new PostResponse(post, currentCount, likescnt, postMember.isPresent()));
    }

    return postResponseList;
  }

  public List<PostResponse> getPostListByMember(User user) {
    List<PostMember> postMemberList = postMemberRepository.findAllByUser(user);
    List<Post> postList = new ArrayList<>();
    List<PostResponse> postResponseList = new ArrayList<>();

    for (PostMember postMember : postMemberList) {
      postList.add(postMember.getPost());
    }

    for (Post post : postList) {
      Optional<PostMember> postMember = Optional.ofNullable(
          postMemberRepository.findByPostAndUser(post, user));
      Integer currentCount = postMemberRepository.findAllByPost(post).size();
      Integer likescnt = postLikeRepository.findAllByPost(post).size();
      post.updateStatus(currentCount);
      postResponseList.add(new PostResponse(post, currentCount, likescnt, postMember.isPresent()));
    }

    return postResponseList;
  }

  public List<PostResponse> getMyPostList(User user) {
    List<Post> postList = postRepository.findAllByUser(user);
    List<PostResponse> postResponseList = new ArrayList<>();

    for (Post post : postList) {
      Optional<PostMember> postMember = Optional.ofNullable(
          postMemberRepository.findByPostAndUser(post, user));
      Integer currentCount = postMemberRepository.findAllByPost(post).size();
      Integer likescnt = postLikeRepository.findAllByPost(post).size();
      post.updateStatus(currentCount);
      postResponseList.add(new PostResponse(post, currentCount, likescnt, postMember.isPresent()));
    }

    return postResponseList;
  }

  public List<PostResponse> getPostListByTitle(String title, User user) {
    List<Post> postList = postRepository.findAll();
    List<PostResponse> postResponseList = new ArrayList<>();

    for (Post post : postList) {
      if(post.getTitle().contains(title)) {
        Optional<PostMember> postMember = Optional.ofNullable(
            postMemberRepository.findByPostAndUser(post, user));
        Integer currentCount = postMemberRepository.findAllByPost(post).size();
        Integer likescnt = postLikeRepository.findAllByPost(post).size();
        post.updateStatus(currentCount);
        postResponseList.add(new PostResponse(post, currentCount, likescnt, postMember.isPresent()));
      }
    }

    return postResponseList;
  }


  public PostResponse getPostById(Long postid, User user) {
    Post post = postRepository.findById(postid).orElseThrow(
        ()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
    );
    Optional<PostMember> postMember = Optional.ofNullable(
        postMemberRepository.findByPostAndUser(post, user));
    Integer currentCount = postMemberRepository.findAllByPost(post).size();
    Integer likescnt = postLikeRepository.findAllByPost(post).size();
    post.updateStatus(currentCount);

    return new PostResponse(post, currentCount, likescnt, postMember.isPresent());
  }

  public PostResponse createPost(PostRequest postRequest, User user) {
    Post post = postRepository.save(new Post(postRequest, user));
    postMemberRepository.save(new PostMember(post, user));

    Optional<PostMember> postMember = Optional.ofNullable(
        postMemberRepository.findByPostAndUser(post, user));
    Integer currentCount = postMemberRepository.findAllByPost(post).size();
    Integer likescnt = postLikeRepository.findAllByPost(post).size();
    post.updateStatus(currentCount);

    return new PostResponse(post, currentCount, likescnt, postMember.isPresent());
  }

  public PostResponse updatePost(Long postid, PostRequest postRequest, User user) {
    Post post = postRepository.findById(postid).orElseThrow(
        ()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
    );

    if(!post.getUser().getUsername().equals(user.getUsername()))
      throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");

    post.update(postRequest);
    Optional<PostMember> postMember = Optional.ofNullable(
        postMemberRepository.findByPostAndUser(post, user));
    Integer currentCount = postMemberRepository.findAllByPost(post).size();
    Integer likescnt = postLikeRepository.findAllByPost(post).size();
    post.updateStatus(currentCount);

    return new PostResponse(post, currentCount, likescnt, postMember.isPresent());
  }

  public String deletePost(Long postid, User user) {
    Post post = postRepository.findById(postid).orElseThrow(
        ()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
    );

    if(!post.getUser().getUsername().equals(user.getUsername()))
      throw new IllegalArgumentException("해당 게시글을 삭제할 권한이 없습니다.");

    List<Comment> commentList = commentRepository.findAllByPost(post);

    for (Comment comment : commentList) {
      commentRepository.deleteById(comment.getId());
    }

    postRepository.deleteById(postid);

    return "삭제되었습니다.";
  }

  public String likePost(Long postid, User user) {
    Post post = postRepository.findById(postid).orElseThrow(
        ()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
    );

    if(post.getUser().getUsername().equals(user.getUsername()))
      throw new IllegalArgumentException("자신의 게시물에는 좋아요를 할 수 없습니다.");

    Optional<PostLike> postLike = Optional.ofNullable(
        postLikeRepository.findByPostAndUser(post, user));
    if(postLike.isPresent()){
      postLikeRepository.deleteById(postLike.get().getId());
      return "게시물에 좋아요를 취소합니다.";
    }
    else {
      postLikeRepository.save(new PostLike(post, user));
      return "게시물에 좋아요를 누르셨습니다.";
    }
  }

  public String joinPost(Long postid, User user) {
    Post post = postRepository.findById(postid).orElseThrow(
        ()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
    );

    if(post.getUser().getUsername().equals(user.getUsername()))
      throw new IllegalArgumentException("자신의 게시물에는 반드시 참여해야 합니다.");

    Optional<PostMember> postMember = Optional.ofNullable(
        postMemberRepository.findByPostAndUser(post, user));
    if(postMember.isPresent()){
      postMemberRepository.deleteById(postMember.get().getId());
      return "게시물에 참여를 취소합니다.";
    }
    else {
      postMemberRepository.save(new PostMember(post, user));
      return "게시물에 참여하셨습니다.";
    }
  }
}
