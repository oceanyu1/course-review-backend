package com.oceancode.coursereview.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceancode.coursereview.domain.dtos.CourseJsonDto;
import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.domain.entities.Department;
import com.oceancode.coursereview.repositories.CourseRepository;
import com.oceancode.coursereview.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Inject the object mapper for JSON parsing
    @Autowired
    private ObjectMapper objectMapper;

    // Inject the resource loader to find files in the resources directory
    @Autowired
    private ResourceLoader resourceLoader;

    // Regex to split "COMP 1005" into ["COMP", "1005"]
    private static final Pattern COURSE_CODE_PATTERN = Pattern.compile("^([A-Z]+)\\s*(\\d+)$");

    @Override
    public void run(String... args) throws Exception {
        // Only load if the database is empty
        if (courseRepository.count() == 0) {
            loadAllCarletonData("carleton_ALL_courses.json");
        }
    }

    private void loadAllCarletonData(String fileName) {
        System.out.println("--- Starting database initialization from JSON ---");

        // 1. Delete all existing courses (child table) first
        courseRepository.deleteAll();
        // 2. Delete all existing departments (parent table)
        departmentRepository.deleteAll();

        try {
            // 1. Locate and read the JSON file from the 'resources' folder
            Resource resource = resourceLoader.getResource("classpath:" + fileName);

            // Use an InputStream to read the file contents
            try (InputStream inputStream = resource.getInputStream()) {

                // 2. Parse the JSON array into a List of CourseJsonDto objects
                List<CourseJsonDto> courseDtos = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<CourseJsonDto>>() {}
                );

                // Map to store all created departments (Key: "COMP", Value: Department object)
                Map<String, Department> departmentMap = new HashMap<>();

                // List to hold all final course entities
                List<Course> courseEntities = new java.util.ArrayList<>();

                // 3. Process each DTO to create Departments and Courses
                for (CourseJsonDto dto : courseDtos) {

                    // --- Parse Code to Get Department and Number ---
                    Matcher matcher = COURSE_CODE_PATTERN.matcher(dto.getCode());
                    if (!matcher.matches()) {
                        System.out.println("Skipping malformed course code: " + dto.getCode());
                        continue;
                    }

                    String deptCode = matcher.group(1).trim();
                    int courseNumber = Integer.parseInt(matcher.group(2).trim());

                    // --- Handle Department (Create if not exists) ---
                    Department department = departmentMap.computeIfAbsent(deptCode, code -> {
                        // NOTE: You'll need a way to get the full name (e.g., "Computer Science")
                        // For now, we use the code as the name, or you can manually define a lookup map.
                        String deptName = code + " Department"; // Fallback name
                        System.out.println("Creating new Department: " + deptCode);
                        return Department.builder().departmentCode(code).name(deptName).build();
                    });

                    // --- Create Course Entity ---
                    Course course = Course.builder()
                            .department(department)
                            .courseNumber(courseNumber)
                            .name(dto.getName())
                            .averageRating(0.0F)
                            .description(dto.getDescription())
                            .build();

                    courseEntities.add(course);
                }

                // 4. Save all entities to the database (important: save departments first)
                departmentRepository.saveAll(departmentMap.values());
                courseRepository.saveAll(courseEntities);

                System.out.println("--- Database initialization complete! ---");
                System.out.println(String.format("Saved %d departments and %d courses.",
                        departmentMap.size(), courseEntities.size()));

            }
        } catch (Exception e) {
            System.err.println("FATAL: Could not load data from JSON file: " + fileName);
            e.printStackTrace();
        }
    }
}