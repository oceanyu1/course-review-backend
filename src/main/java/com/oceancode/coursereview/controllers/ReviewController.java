package com.oceancode.coursereview.controllers;

import com.oceancode.coursereview.domain.dtos.*;
import com.oceancode.coursereview.domain.entities.Review;
import com.oceancode.coursereview.domain.entities.User;
import com.oceancode.coursereview.services.ReviewService;
import com.oceancode.coursereview.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping(path = "/{courseId}")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable UUID courseId,
            @Valid @RequestBody CreateReviewRequest request) {

        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .difficulty(request.getDifficulty())
                .workload(request.getWorkload())
                .anonymous(request.getAnonymous())
                .build();

        Review savedReview = reviewService.createReview(review, courseId, currentUser);
        return ResponseEntity.ok(convertToReviewResponse(savedReview));
    }

    @GetMapping(path = "/{courseId}")
    public List<ReviewResponse> getReviews(
            @PathVariable UUID courseId,
            @RequestParam(defaultValue = "rating_asc") String sortBy) {
        List<Review> reviews = reviewService.listReviewsByCourseId(courseId, sortBy);
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(this::convertToReviewResponse)
                .toList();
        return reviewResponses;
    }

    @PutMapping(path = "/{courseId}/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable UUID courseId,
            @PathVariable UUID reviewId,
            @Valid @RequestBody CreateReviewRequest request) {

        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .difficulty(request.getDifficulty())
                .workload(request.getWorkload())
                .anonymous(request.getAnonymous())
                .build();

        Review updatedReview = reviewService.updateReview(
                courseId, reviewId, review, currentUser
        );

        return ResponseEntity.ok(convertToReviewResponse(updatedReview));
    }

    @PatchMapping(path = "/{courseId}/{reviewId}")
    public ResponseEntity<ReviewResponse> partialUpdateReview(
            @PathVariable UUID courseId,
            @PathVariable UUID reviewId,
            @RequestBody CreateReviewRequest request) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .difficulty(request.getDifficulty())
                .workload(request.getWorkload())
                .anonymous(request.getAnonymous())
                .build();

        Review updatedReview = reviewService.partialUpdateReview(
                courseId, reviewId, review, currentUser
        );

        return ResponseEntity.ok(convertToReviewResponse(updatedReview));
    }

    @DeleteMapping(path = "/{courseId}/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID courseId,
            @PathVariable UUID reviewId) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        reviewService.deleteReview(courseId, reviewId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/has-reviewed")
    public ResponseEntity<Boolean> hasUserReviewedCourse(@PathVariable UUID courseId) {
        // Get current authenticated user (same logic you already use)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        // This method will use your new existsBy... repository method
        boolean hasReviewed = reviewService.hasUserReviewed(courseId, currentUser);

        return ResponseEntity.ok(hasReviewed);
    }

    @GetMapping("/me")
    public List<ReviewResponse> getReviewsByUser() {
        // Get current authenticated user (same logic you already use)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        // This method will use your new existsBy... repository method
        List<Review> userReviews = reviewService.getReviewsByUser(currentUser);

        List<ReviewResponse> userReviewResponses = userReviews.stream()
                .map(this::convertToReviewResponse)
                .toList();
        return userReviewResponses;
    }

    private ReviewResponse convertToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .difficulty(review.getDifficulty())
                .workload(review.getWorkload())
                .datePosted(review.getDatePosted())
                .lastEdited(review.getLastEdited())
                .anonymous(review.getAnonymous())
                .writtenBy(convertToUserDto(review.getWrittenBy(), review.getAnonymous()))
                .courseId(review.getCourse().getId())
                .courseNumber(review.getCourse().getCourseNumber())
                .courseTitle(review.getCourse().getName())
                .build();
    }

    private UserDto convertToUserDto(User user, Boolean isAnonymous) {
        boolean anonymous = isAnonymous != null ? isAnonymous : false;
        if (anonymous) {
            return UserDto.builder()
                    .id(user.getId())
                    .name("Anonymous")
                    .program(null)
                    .year(null)
                    .build();
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .program(user.getProgram())
                .year(user.getYear())
                .build();
    }
}
