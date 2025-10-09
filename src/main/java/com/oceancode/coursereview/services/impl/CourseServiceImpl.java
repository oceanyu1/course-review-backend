package com.oceancode.coursereview.services.impl;

import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.exceptions.CourseNotFoundException;
import com.oceancode.coursereview.repositories.CourseRepository;
import com.oceancode.coursereview.repositories.ReviewRepository;
import com.oceancode.coursereview.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<Course> getCoursesByDepartment(String departmentCode, String sortBy) {
        Sort sort = createSort(sortBy);
        return courseRepository.findByDepartment_DepartmentCode(departmentCode, sort);
    }

    @Override
    public Page<Course> searchCourses(String query, String departmentCode, Pageable pageable) {
        String processedQuery = query;
        String effectiveDepartmentCode = departmentCode;

        if (query != null && !query.isEmpty() && (departmentCode == null || departmentCode.trim().isEmpty())) {
            // Check if query contains department code pattern (e.g., "COMP 1405")
            String[] parts = query.trim().split("\\s+");
            if (parts.length >= 2 && parts[0].matches("(?i)[A-Z]{3,4}")) {
                effectiveDepartmentCode = parts[0].toUpperCase();
                processedQuery = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
            }
        }

        // If no search query, return all courses (filtered by department if specified)
        if (processedQuery == null || processedQuery.trim().isEmpty()) {
            if (effectiveDepartmentCode != null && !effectiveDepartmentCode.trim().isEmpty()) {
                List<Course> courses = getCoursesByDepartment(effectiveDepartmentCode,
                        pageable.getSort().toString().replace(": ", "_").toLowerCase());

                return new PageImpl<>(courses, pageable, courses.size());
            } else {
                // Return all courses across all departments
                return courseRepository.findAll(pageable);
            }
        }

        // Regular search with query
        if (effectiveDepartmentCode != null && !effectiveDepartmentCode.trim().isEmpty()) {
            List<Course> courses = courseRepository.searchByQueryAndDepartment(processedQuery,
                    effectiveDepartmentCode, createSortString(pageable.getSort()));
            return new PageImpl<>(courses, pageable, courses.size());
        } else {
            return courseRepository.searchByQuery(processedQuery, pageable);
        }
    }

    private String createSortString(Sort sort) {
        if (sort.isEmpty()) return "courseNumber_asc";
        Sort.Order order = sort.iterator().next();
        String direction = order.getDirection().isAscending() ? "_asc" : "_desc";
        return order.getProperty() + direction;
    }


    private Sort createSort(String sortBy) {
        return switch (sortBy) {
            case "courseNumber_asc" -> Sort.by("courseNumber").ascending();
            case "courseNumber_desc" -> Sort.by("courseNumber").descending();
            case "rating_asc" -> Sort.by("averageRating").ascending();
            case "rating_desc" -> Sort.by("averageRating").descending();
            default -> Sort.by("courseNumber").ascending();
        };
    }

    @Override
    public Optional<Course> getCourseById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    public void updateCourseAverageRating(UUID courseId) {
        Double averageRating = courseRepository.getAverageRatingByCourse_Id(courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course does not exist"));

        course.setAverageRating(averageRating != null ? averageRating.floatValue() : 0.0f);
        courseRepository.save(course);
    }

    @Transactional
    public void recalculateAllCourseRatings() {
        List<Course> allCourses = (List<Course>) courseRepository.findAll();

        for (Course course : allCourses) {
            updateCourseAverageRating(course.getId());
        }
    }

}
