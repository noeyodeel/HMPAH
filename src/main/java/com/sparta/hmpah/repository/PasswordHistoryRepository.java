package com.sparta.hmpah.repository;

import com.sparta.hmpah.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
}
