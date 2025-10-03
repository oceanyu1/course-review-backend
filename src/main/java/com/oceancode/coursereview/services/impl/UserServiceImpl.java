package com.oceancode.coursereview.services.impl;

import com.oceancode.coursereview.domain.entities.User;
import com.oceancode.coursereview.exceptions.UserNotFoundException;
import com.oceancode.coursereview.repositories.UserRepository;
import com.oceancode.coursereview.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
