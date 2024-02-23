package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Follow;
import com.sparta.hmpah.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

  List<User> findAllByFollower(User user);
}
