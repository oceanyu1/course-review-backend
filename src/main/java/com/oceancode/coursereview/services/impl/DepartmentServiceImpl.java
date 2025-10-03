package com.oceancode.coursereview.services.impl;

import com.oceancode.coursereview.domain.entities.Department;
import com.oceancode.coursereview.repositories.DepartmentRepository;
import com.oceancode.coursereview.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return StreamSupport.stream(departmentRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }
}
