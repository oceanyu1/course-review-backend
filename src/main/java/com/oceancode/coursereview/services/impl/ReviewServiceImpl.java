package com.oceancode.coursereview.services.impl;

import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.domain.entities.Review;
import com.oceancode.coursereview.domain.entities.User;
import com.oceancode.coursereview.exceptions.CourseNotFoundException;
import com.oceancode.coursereview.exceptions.ReviewNotAllowedException;
import com.oceancode.coursereview.exceptions.ReviewNotFoundException;
import com.oceancode.coursereview.repositories.CourseRepository;
import com.oceancode.coursereview.repositories.ReviewRepository;
import com.oceancode.coursereview.services.CourseService;
import com.oceancode.coursereview.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Transactional
    @Override
    public Review createReview(Review review, UUID courseId, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course does not exist"));

        if (hasUserReviewed(courseId, user)) {
            throw new ReviewNotAllowedException("You have already submitted a review for this course.");
        }

        review.setCourse(course);
        review.setWrittenBy(user);
        review.setDatePosted(Instant.now());

        Review savedReview = reviewRepository.save(review);
        courseService.updateCourseAverageRating(review.getCourse().getId());
        return savedReview;
    }

    @Override
    public List<Review> listReviewsByCourseId(UUID courseId, String sortBy) {
        Sort sort = createSort(sortBy);
        return reviewRepository.findByCourse_Id(courseId, sort);
    }

    private Sort createSort(String sortBy) {
        return switch (sortBy) {
            case "rating_asc" -> Sort.by("rating").ascending();
            case "rating_desc" -> Sort.by("rating").descending();
            case "difficulty_asc" -> Sort.by("difficulty").ascending();
            case "difficulty_desc" -> Sort.by("difficulty").descending();
            case "workload_asc" -> Sort.by("workload").ascending();
            case "workload_desc" -> Sort.by("workload").descending();
            default -> Sort.by("rating").ascending();
        };
    }

    @Override
    public Optional<Review> getReviewById(UUID reviewId) {
        return reviewRepository.findById(reviewId);
    }

    @Transactional
    @Override
    public Review updateReview(UUID courseId, UUID reviewId, Review updatedReview, User user) {
        Review existingReview = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review does not exist"));

        if (!existingReview.getWrittenBy().getId().equals(user.getId())) {
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }

        if (Instant.now().isAfter(existingReview.getDatePosted().plus(Duration.ofHours(48)))) {
            throw new ReviewNotAllowedException("Review can no longer be edited");
        }

        if (!existingReview.getCourse().getId().equals(courseId)) {
            throw new ReviewNotFoundException("Review does not belong to this course");
        }

        updatedReview.setId(reviewId);
        updatedReview.setCourse(existingReview.getCourse());
        updatedReview.setWrittenBy(existingReview.getWrittenBy());
        updatedReview.setAnonymous(existingReview.getAnonymous());
        updatedReview.setDatePosted(existingReview.getDatePosted());
        updatedReview.setLastEdited(Instant.now());

        Review savedReview = reviewRepository.save(updatedReview);

        courseService.updateCourseAverageRating(existingReview.getCourse().getId());

        return savedReview;
    }

    @Transactional
    @Override
    public Review partialUpdateReview(UUID courseId, UUID reviewId, Review reviewPatch, User user) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review does not exist"));

        if (!existingReview.getWrittenBy().getId().equals(user.getId())) {
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }

        if (Instant.now().isAfter(existingReview.getDatePosted().plus(Duration.ofHours(48)))) {
            throw new ReviewNotAllowedException("Review can no longer be edited");
        }

        if (!existingReview.getCourse().getId().equals(courseId)) {
            throw new ReviewNotFoundException("Review does not belong to this course");
        }

        Optional.ofNullable(reviewPatch.getRating()).ifPresent(existingReview::setRating);
        Optional.ofNullable(reviewPatch.getContent()).ifPresent(existingReview::setContent);
        Optional.ofNullable(reviewPatch.getDifficulty()).ifPresent(existingReview::setDifficulty);
        Optional.ofNullable(reviewPatch.getWorkload()).ifPresent(existingReview::setWorkload);
        Optional.ofNullable(reviewPatch.getAnonymous()).ifPresent(existingReview::setAnonymous);

        existingReview.setLastEdited(Instant.now());

        Review savedReview = reviewRepository.save(existingReview);
        courseService.updateCourseAverageRating(existingReview.getCourse().getId());

        return savedReview;
    }

    @Transactional
    @Override
    public void deleteReview(UUID courseId, UUID reviewId, User user) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review does not exist"));

        if (!existingReview.getWrittenBy().getId().equals(user.getId())) {
            throw new ReviewNotAllowedException("Cannot delete another user's review");
        }

        if (!existingReview.getCourse().getId().equals(courseId)) {
            throw new ReviewNotFoundException("Review does not belong to this course");
        }

        reviewRepository.delete(existingReview);
        courseService.updateCourseAverageRating(existingReview.getCourse().getId());
    }

    @Override
    public boolean hasUserReviewed(UUID courseId, User currentUser) {
        return reviewRepository.existsByCourse_IdAndWrittenBy_Id(courseId, currentUser.getId());
    }
}
