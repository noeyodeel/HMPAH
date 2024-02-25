package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.Follow;
import com.sparta.hmpah.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    //By() -> 내 역할
    List<Follow> findByFollower(User user);
    List<Follow> findByFollowing(User user);
    //테스트 해야댐
    void deleteByFollowerAndFollowing(User follower, User following);
}
