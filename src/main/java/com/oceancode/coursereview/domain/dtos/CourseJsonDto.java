package com.oceancode.coursereview.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseJsonDto {
    private String code;     // Matches "AERO 2001"
    private String name;
    private String description;
}
