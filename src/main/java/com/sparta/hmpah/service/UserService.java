package com.sparta.hmpah.service;

import com.sparta.hmpah.repository.FollowRepository;
import com.sparta.hmpah.repository.PasswordHistoryRepository;
import com.sparta.hmpah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final FollowRepository followRepository;
}
