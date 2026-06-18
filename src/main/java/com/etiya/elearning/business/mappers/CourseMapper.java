package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.requests.CreateCourseRequest;
import com.etiya.elearning.business.requests.UpdateCourseRequest;
import com.etiya.elearning.business.responses.CourseResponse;
import com.etiya.elearning.entities.concretes.Course;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Course entity'si ile DTO'ları arasında dönüşüm yapar. İlişkili entity'ler
 * (category/instructor/language) Manager tarafından set edilir; mapper yalnızca
 * skaler alanları taşır. Entity asla controller'a sızdırılmaz.
 */
@Component
public class CourseMapper {

    public Course toEntity(CreateCourseRequest request) {
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setPrice(request.getPrice());
        course.setDescription(request.getDescription());
        course.setDifficulty(request.getDifficulty());
        return course;
    }

    public void updateEntityFromRequest(Course course, UpdateCourseRequest request) {
        course.setCourseName(request.getCourseName());
        course.setPrice(request.getPrice());
        course.setDescription(request.getDescription());
        course.setDifficulty(request.getDifficulty());
    }

    public CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCategory().getId(),
                course.getInstructor().getId(),
                course.getLanguage().getId(),
                course.getCourseName(),
                course.getPrice(),
                course.getDescription(),
                course.getDifficulty()
        );
    }

    public List<CourseResponse> toResponseList(List<Course> courses) {
        return courses.stream().map(this::toResponse).toList();
    }
}
