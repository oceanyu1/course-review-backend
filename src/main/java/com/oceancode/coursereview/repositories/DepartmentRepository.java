package com.oceancode.coursereview.repositories;

import com.oceancode.coursereview.domain.entities.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, UUID> {
}
