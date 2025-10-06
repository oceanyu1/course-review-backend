package com.oceancode.coursereview.repositories;

import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.domain.entities.Review;
import com.oceancode.coursereview.domain.entities.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends CrudRepository<Review, UUID> {
    List<Review> findByCourse_Id(UUID courseId, Sort sort);

    boolean existsByCourse_IdAndWrittenBy_Id(UUID courseId, UUID id);

    List<Review> findByWrittenBy_Id(UUID userId);

    void deleteByWrittenBy(User writtenBy);

    @Query("SELECT r.course.id FROM Review r WHERE r.writtenBy = :user")
    List<UUID> findCourseIdsByWrittenBy(User user);
}
