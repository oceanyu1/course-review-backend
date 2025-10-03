package com.oceancode.coursereview.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 1000)
    private String content;

    private Integer rating;

    private Integer difficulty;

    private Integer workload;

    private Instant datePosted;

    private Instant lastEdited;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writtenBy;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    @Builder.Default
    private Boolean anonymous = false;
}
