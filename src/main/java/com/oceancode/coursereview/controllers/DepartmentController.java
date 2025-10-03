package com.oceancode.coursereview.controllers;

import com.oceancode.coursereview.domain.dtos.DepartmentDto;
import com.oceancode.coursereview.domain.entities.Department;
import com.oceancode.coursereview.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public List<DepartmentDto> getAllDepartments() {
        return departmentService.getAllDepartments()
                .stream()
                .map(this::convertToDepartmentDto)
                .toList();
    }

    private DepartmentDto convertToDepartmentDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .departmentCode(department.getDepartmentCode())
                .name(department.getName())
                .build();
    }
}
