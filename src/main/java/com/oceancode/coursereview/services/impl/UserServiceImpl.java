package com.oceancode.coursereview.services.impl;

import com.oceancode.coursereview.domain.entities.User;
import com.oceancode.coursereview.exceptions.UserNotFoundException;
import com.oceancode.coursereview.repositories.ReviewRepository;
import com.oceancode.coursereview.repositories.UserRepository;
import com.oceancode.coursereview.services.CourseService;
import com.oceancode.coursereview.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CourseService courseService;

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

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        List<UUID> affectedCourseIds = reviewRepository.findCourseIdsByWrittenBy(user);

        reviewRepository.deleteByWrittenBy(user);
        userRepository.delete(user);

        for (UUID courseId : affectedCourseIds) {
            courseService.updateCourseAverageRating(courseId);
        }
        //courseService.recalculateAllCourseRatings();
    }
}
