package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.dto.responseDto.PostResponse;
import com.sparta.hmpah.entity.Comment;
import com.sparta.hmpah.entity.Follow;
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

  public List<PostResponse> getPostListByFollow(User user) {
    List<Follow> followings = followRepository.findByFollower(user);
    List<User> followingUserList = new ArrayList<>();
    for (Follow following : followings) {
      followingUserList.add(following.getFollowing());
    }
    List<Post> postList = new ArrayList<>();

    for (User following : followingUserList) {
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

  public PostResponse getPostById(Long postid, User user) {
    Post post = getPostById(postid);
    return createPostResponse(post, user);
  }

  @Transactional
  public PostResponse createPost(PostRequest postRequest, User user) {
    if(postRequest.getMaxcount()<1)
      throw new IllegalArgumentException("모집인원은 0보다 커야합니다.");
    Post post = postRepository.save(new Post(postRequest, user));
    postMemberRepository.save(new PostMember(post, user));
    post.updateStatus(getCurrentCount(post));

    return createPostResponse(post, user);
  }

  @Transactional
  public PostResponse updatePost(Long postid, PostRequest postRequest, User user) {
    Post post = getPostById(postid);

    if(!getIsOwner(post, user))
      throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");

    if(getCurrentCount(post)>postRequest.getMaxcount())
      throw new IllegalArgumentException("모집인원은 현재 인원보다 커야 합니다.");

    post.update(postRequest);
    post.updateStatus(getCurrentCount(post));
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

    if(post.getUser().getId().equals(user.getId()))
      throw new IllegalArgumentException("자신의 게시물에는 반드시 참여해야 합니다.");

    Optional<PostMember> postMember = Optional.ofNullable(
        postMemberRepository.findByPostAndUser(post, user));
    if(postMember.isPresent()){
      postMemberRepository.deleteById(postMember.get().getId());
      post.updateStatus(getCurrentCount(post));
      return "게시물에 참여를 취소합니다.";
    }
    else {
      if(post.getStatus().equals(PostStatusEnum.COMPLETED))
        return "모집인원이 가득 찼습니다.";
      postMemberRepository.save(new PostMember(post, user));
      post.updateStatus(getCurrentCount(post));
      return "게시물에 참여하셨습니다.";
    }
  }
  private Integer getCurrentCount(Post post){
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

  public List<PostResponse> createPostResponseList(List<Post> postList, User user){
    List<PostResponse> postResponseList = new ArrayList<>();
    for (Post post : postList) {
      postResponseList.add(new PostResponse(post, getCurrentCount(post), getLikescnt(post), getIsMember(post, user)));
    }
    return postResponseList;
  }


  public PostResponse createPostResponse(Post post, User user){
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

  public List<PostResponse> getPostListByOption(String status, String location, String title, User user) {
    Boolean isStatus = !(status==null);
    Boolean isLocation = !(location==null);
    Boolean isTitle = !(title==null);
    List<Post> postList = postRepository.findAll();
    if(isStatus)
      postList = filterByStatus(postList, status);
    if(isLocation)
      postList = filterByLocation(postList, location);
    if(isTitle)
      postList = filterByTitle(postList, title);
    return createPostResponseList(postList, user);
  }

  private List<Post> filterByStatus(List<Post> postList, String status){
    List<Post> posts = new ArrayList<>();
    for (Post post : postList) {
      if(post.getStatus().getLabel().equals(status))
        posts.add(post);
    }
    return posts;
  }

  private List<Post> filterByLocation(List<Post> postList, String location){
    List<Post> posts = new ArrayList<>();
    for (Post post : postList) {
      if(post.getLocation().getLabel().equals(location))
        posts.add(post);
    }
    return posts;
  }

  private List<Post> filterByTitle(List<Post> postList, String title){
    List<Post> posts = new ArrayList<>();
    for (Post post : postList) {
      if(post.getTitle().contains(title))
        posts.add(post);
    }
    return posts;
  }


}
