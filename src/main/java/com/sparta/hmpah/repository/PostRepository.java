package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Post;
import com.sparta.hmpah.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByStatus(String status);

  List<Post> findAllByLocation(String location);

  List<Post> findAllByUser(User following);
}
