package com.oceancode.coursereview.services.impl;

import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.exceptions.CourseNotFoundException;
import com.oceancode.coursereview.repositories.CourseRepository;
import com.oceancode.coursereview.repositories.ReviewRepository;
import com.oceancode.coursereview.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Course> searchCourses(String query, String departmentCode, String sortBy) {
        if (departmentCode != null && !departmentCode.isEmpty()) {
            // Search within specific department
            return courseRepository.searchByQueryAndDepartment(query, departmentCode, sortBy);
        } else {
            // Search across all departments
            return courseRepository.searchByQuery(query, sortBy);
        }
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
