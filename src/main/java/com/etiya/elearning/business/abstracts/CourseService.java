package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.CreateCourseRequest;
import com.etiya.elearning.business.requests.UpdateCourseRequest;
import com.etiya.elearning.business.responses.CourseResponse;

import java.util.List;

public interface CourseService {

    CourseResponse add(CreateCourseRequest request);

    CourseResponse update(UpdateCourseRequest request);

    void delete(Long id);

    CourseResponse getById(Long id);

    List<CourseResponse> getAll();
}
