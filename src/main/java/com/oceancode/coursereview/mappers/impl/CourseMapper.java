package com.oceancode.coursereview.mappers.impl;

import com.oceancode.coursereview.domain.dtos.CourseDto;
import com.oceancode.coursereview.domain.entities.Course;
import com.oceancode.coursereview.mappers.Mapper;

public class CourseMapper implements Mapper<Course, CourseDto> {

    @Override
    public CourseDto mapTo(Course course) {
        return null;
    }

    @Override
    public Course mapFrom(CourseDto courseDto) {
        return null;
    }
}
