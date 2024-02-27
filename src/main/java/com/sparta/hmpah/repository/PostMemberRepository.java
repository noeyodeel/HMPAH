package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.PostLike;
import com.sparta.hmpah.entity.PostMember;
import com.sparta.hmpah.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMemberRepository extends JpaRepository<PostMember, Long> {

  List<PostMember> findAllByPost(Post post);

  List<PostMember> findAllByUser(User user);

  PostMember findByPostAndUser(Post post, User user);


  void deleteAllByPost(Post post);
}
