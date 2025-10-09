package com.oceancode.coursereview.services;

import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.domain.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {
    List<Course> getCoursesByDepartment(String departmentCode, String sortBy);
    Page<Course> searchCourses(String query, String departmentCode, Pageable pageable);
    Optional<Course> getCourseById(UUID courseId);
    void updateCourseAverageRating(UUID courseId);
    void recalculateAllCourseRatings();
}
