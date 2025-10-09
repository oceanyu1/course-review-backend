package com.oceancode.coursereview.repositories;

import com.oceancode.coursereview.domain.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByDepartment_DepartmentCode(String departmentCode, Sort sort);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.id = :courseId")
    Double getAverageRatingByCourse_Id(UUID courseId);

    @Query("SELECT c FROM Course c WHERE c.department.departmentCode = :deptCode AND " +
            "(CAST(c.courseNumber AS string) LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'courseNumber_asc' THEN c.courseNumber END ASC, " +
            "CASE WHEN :sortBy = 'courseNumber_desc' THEN c.courseNumber END DESC, " +
            "CASE WHEN :sortBy = 'rating_asc' THEN c.averageRating END ASC, " +
            "CASE WHEN :sortBy = 'rating_desc' THEN c.averageRating END DESC")
    List<Course> searchByQueryAndDepartment(@Param("query") String query,
                                            @Param("deptCode") String departmentCode,
                                            @Param("sortBy") String sortBy);

    @Query("SELECT c FROM Course c WHERE " +
            "CAST(c.courseNumber AS string) LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Course> searchByQuery(@Param("query") String query,
                               Pageable pageable);
}

