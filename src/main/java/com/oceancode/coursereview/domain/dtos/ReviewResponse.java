package com.oceancode.coursereview.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private UUID id;
    private String content;
    private Integer rating;
    private Integer difficulty;
    private Integer workload;
    private Instant datePosted;
    private Instant lastEdited;
    private UserDto writtenBy;
    private UUID courseId;
    private Boolean anonymous;
}
