package com.oceancode.coursereview.services;

import com.oceancode.coursereview.domain.entities.Review;
import com.oceancode.coursereview.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewService {
    Review createReview(Review review, UUID courseId, User user);
    List<Review> listReviewsByCourseId(UUID courseId, String sortBy);
    Optional<Review> getReviewById(UUID reviewId);
    Review updateReview(UUID courseId, UUID reviewId, Review updatedReview, User user);
    Review partialUpdateReview(UUID courseId, UUID reviewId, Review reviewPatch, User user);
    void deleteReview(UUID courseId, UUID reviewId, User user);

    boolean hasUserReviewed(UUID courseId, User currentUser);
}

