package com.oceancode.coursereview.controllers;

import com.oceancode.coursereview.domain.dtos.CourseDto;
import com.oceancode.coursereview.domain.dtos.DepartmentDto;
import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.domain.entities.Department;
import com.oceancode.coursereview.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:4173",
        "https://course-review-frontend-ivory.vercel.app"}, allowCredentials = "true",
            allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/department/{departmentCode}")
    public List<CourseDto> getCoursesByDepartment(
            @PathVariable String departmentCode,
            @RequestParam(defaultValue = "courseNumber_asc") String sortBy) {
        List<Course> courses = courseService.getCoursesByDepartment(departmentCode, sortBy);
        List<CourseDto> courseDtos = courses.stream()
                .map(this::convertToCourseDto)
                .toList();
        return courseDtos;
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable UUID courseId) {
        return courseService.getCourseById(courseId)
                .map(this::convertToCourseDto)           // Convert if present
                .map(ResponseEntity::ok)                 // Wrap in 200 response
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public Page<CourseDto> searchCourses(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "60") int size,
            @RequestParam(defaultValue = "courseNumber_asc") String sortBy) {
        Sort sort = createSort(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Course> coursePage = courseService.searchCourses(query, departmentCode, pageable);
        return coursePage.map(this::convertToCourseDto);
    }

    private CourseDto convertToCourseDto(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .department(convertToDepartmentDto(course.getDepartment()))
                .courseNumber(course.getCourseNumber())
                .name(course.getName())
                .averageRating(course.getAverageRating())
                .description(course.getDescription())
                .build();
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

    private DepartmentDto convertToDepartmentDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .departmentCode(department.getDepartmentCode())
                .name(department.getName())
                .build();
    }
}
