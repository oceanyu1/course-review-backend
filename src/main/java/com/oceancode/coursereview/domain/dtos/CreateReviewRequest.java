package com.oceancode.coursereview.domain.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReviewRequest {
    private String content;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @NotNull(message = "Difficulty is required")
    @Min(value = 1, message = "Difficulty must be between 1 and 5")
    @Max(value = 5, message = "Difficulty must be between 1 and 5")
    private Integer difficulty;

    @NotNull(message = "Workload is required")
    @Min(value = 1, message = "Workload must be between 1 and 5")
    @Max(value = 5, message = "Workload must be between 1 and 5")
    private Integer workload;

    @NotNull
    @Builder.Default
    private Boolean anonymous = false;
}
