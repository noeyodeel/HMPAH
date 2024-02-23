package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.PostLike;
import com.sparta.hmpah.entity.PostMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMemberRepository extends JpaRepository<PostMember, Long> {
}
