package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.LocationEnum;
import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.PostLike;
import com.sparta.hmpah.entity.PostMember;
import com.sparta.hmpah.entity.PostStatusEnum;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.CommentLikeRepository;
import com.sparta.hmpah.repository.CommentRepository;
import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PostLikeRepository;
import com.sparta.hmpah.repository.PostMemberRepository;
import com.sparta.hmpah.repository.PostRepository;
import com.sparta.hmpah.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PostLikeRepository postLikeRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final PostMemberRepository postMemberRepository;
  private final FollowRepository followRepository;

  public List<PostResponse> getPostList(User user) {
    List<Post> postList = postRepository.findAll();
    return createPostResponseList(postList, user);
  }

  public List<PostResponse> getPostListByStatus(String status, User user) {
    PostStatusEnum postStatusEnum = PostStatusEnum.valueOf(status);
    List<Post> postList = postRepository.findAllByStatus(postStatusEnum);
    return createPostResponseList(postList, user);
  }


  public List<PostResponse> getPostListByLocation(String location, User user) {
    LocationEnum locationEnum = LocationEnum.valueOf(location);
    List<Post> postList = postRepository.findAllByLocation(locationEnum);
    return createPostResponseList(postList, user);
  }

  public List<PostResponse> getPostListByFollow(User user) {
    List<User> followings = followRepository.findAllByFollower(user);
    List<Post> postList = new ArrayList<>();

    for (User following : followings) {
      postList.addAll(postRepository.findAllByUser(following));
    }

    return createPostResponseList(postList, user);
  }

  public List<PostResponse> getPostListByMember(User user) {
    List<PostMember> postMemberList = postMemberRepository.findAllByUser(user);
    List<Post> postList = new ArrayList<>();

    for (PostMember postMember : postMemberList) {
      postList.add(postMember.getPost());
    }

    return createPostResponseList(postList, user);
  }

  public List<PostResponse> getMyPostList(User user) {
    List<Post> postList = postRepository.findAllByUser(user);
    return createPostResponseList(postList, user);
  }

  public List<PostResponse> getPostListByTitle(String title, User user) {
    List<Post> allPost = postRepository.findAll();
    List<Post> postList = new ArrayList<>();

    for (Post post : allPost) {
      if(post.getTitle().contains(title)){
        postList.add(post);
      }
    }

    return createPostResponseList(postList, user);
  }


  public PostResponse getPostById(Long postid, User user) {
    Post post = getPostById(postid);
    return createPostResponse(post, user);
  }

  @Transactional
  public PostResponse createPost(PostRequest postRequest, User user) {
    Post post = postRepository.save(new Post(postRequest, user));
    postMemberRepository.save(new PostMember(post, user));

    return createPostResponse(post, user);
  }

  @Transactional
  public PostResponse updatePost(Long postid, PostRequest postRequest, User user) {
    Post post = getPostById(postid);

    if(!getIsOwner(post, user))
      throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");

    post.update(postRequest);
    return createPostResponse(post, user);
  }

  @Transactional
  public String deletePost(Long postid, User user) {
    Post post = getPostById(postid);

    if(!getIsOwner(post, user))
      throw new IllegalArgumentException("해당 게시글을 삭제할 권한이 없습니다.");

    List<Comment> commentList = commentRepository.findAllByPost(post);

    for (Comment comment : commentList) {
      commentLikeRepository.deleteAllByComment(comment);
      commentRepository.deleteById(comment.getId());
    }
    postMemberRepository.deleteAllByPost(post);
    postLikeRepository.deleteAllByPost(post);
    postRepository.deleteById(postid);

    return "삭제되었습니다.";
  }

  @Transactional
  public String likePost(Long postid, User user) {
    Post post = getPostById(postid);

    if(getIsOwner(post, user))
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

  @Transactional
  public String joinPost(Long postid, User user) {
    Post post = getPostById(postid);

    if(post.getUser().getUsername().equals(user.getUsername()))
      throw new IllegalArgumentException("자신의 게시물에는 반드시 참여해야 합니다.");

    Optional<PostMember> postMember = Optional.ofNullable(
        postMemberRepository.findByPostAndUser(post, user));
    if(postMember.isPresent()){
      postMemberRepository.deleteById(postMember.get().getId());
      return "게시물에 참여를 취소합니다.";
    }
    else {
      if(post.getStatus().equals(PostStatusEnum.COMPLETED))
        return "모집인원이 가득 찼습니다.";
      postMemberRepository.save(new PostMember(post, user));
      return "게시물에 참여하셨습니다.";
    }
  }
  public Integer getCurrentCount(Post post){
    return postMemberRepository.findAllByPost(post).size();
  }

  public Integer getLikescnt(Post post){
    return postLikeRepository.findAllByPost(post).size();
  }

  public Boolean getIsMember(Post post, User user){
    Optional<PostMember> postMember = Optional.ofNullable(
        postMemberRepository.findByPostAndUser(post, user));
    return postMember.isPresent();
  }

  @Transactional
  public List<PostResponse> createPostResponseList(List<Post> postList, User user){
    List<PostResponse> postResponseList = new ArrayList<>();
    for (Post post : postList) {
      post.updateStatus(getCurrentCount(post));
      postResponseList.add(new PostResponse(post, getCurrentCount(post), getLikescnt(post), getIsMember(post, user)));
    }
    return postResponseList;
  }

  @Transactional
  public PostResponse createPostResponse(Post post, User user){
    post.updateStatus(getCurrentCount(post));
    return new PostResponse(post, getCurrentCount(post), getLikescnt(post), getIsMember(post, user));
  }

  public Post getPostById(Long postid){
    return postRepository.findById(postid).orElseThrow(
        ()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
    );
  }

  public Boolean getIsOwner(Post post, User user){
    if(post.getUser().getId().equals(user.getId()))
      return true;
    else
      return false;
  }
}
