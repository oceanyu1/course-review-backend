package com.oceancode.coursereview.services;

import com.oceancode.coursereview.domain.entities.User;

public interface UserService {
    User save(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
