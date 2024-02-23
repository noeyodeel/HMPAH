package com.sparta.hmpah.service;

import com.sparta.hmpah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
}
