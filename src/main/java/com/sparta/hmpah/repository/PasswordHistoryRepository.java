package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.PasswordHistory;
import com.sparta.hmpah.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findByUser(User user);
}
