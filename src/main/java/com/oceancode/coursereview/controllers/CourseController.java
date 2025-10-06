package com.oceancode.coursereview.controllers;

import com.oceancode.coursereview.domain.dtos.CourseDto;
import com.oceancode.coursereview.domain.dtos.DepartmentDto;
import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.domain.entities.Department;
import com.oceancode.coursereview.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:4173"}, allowCredentials = "true",
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
    public List<CourseDto> searchCourses(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(defaultValue = "courseNumber_asc") String sortBy) {
        List<Course> courses = courseService.searchCourses(query, departmentCode, sortBy);
        return courses.stream()
                .map(this::convertToCourseDto)
                .toList();
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

    private DepartmentDto convertToDepartmentDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .departmentCode(department.getDepartmentCode())
                .name(department.getName())
                .build();
    }
}
