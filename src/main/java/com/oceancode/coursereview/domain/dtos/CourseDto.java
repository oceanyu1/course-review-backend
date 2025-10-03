package com.oceancode.coursereview.domain.dtos;

import com.oceancode.coursereview.domain.entities.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    private UUID id;
    private DepartmentDto department;
    private Integer courseNumber;
    private String name;
    private float averageRating;
    private String description;
}
