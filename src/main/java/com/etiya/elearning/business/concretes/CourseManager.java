package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.CourseService;
import com.etiya.elearning.business.mappers.CourseMapper;
import com.etiya.elearning.business.requests.CreateCourseRequest;
import com.etiya.elearning.business.requests.UpdateCourseRequest;
import com.etiya.elearning.business.responses.CourseResponse;
import com.etiya.elearning.business.rules.CourseBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.CourseRepository;
import com.etiya.elearning.entities.concretes.Category;
import com.etiya.elearning.entities.concretes.Course;
import com.etiya.elearning.entities.concretes.Language;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseManager implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseBusinessRules rules;

    @Override
    public CourseResponse add(CreateCourseRequest request) {
        Category category = rules.categoryMustExist(request.getCategoryId());
        rules.categoryMustBeLeaf(request.getCategoryId());
        User instructor = rules.instructorMustExist(request.getInstructorId());
        rules.instructorMustHaveInstructorRole(instructor);
        Language language = rules.languageMustExist(request.getLanguageId());
        rules.priceCannotBeNegative(request.getPrice());

        Course course = courseMapper.toEntity(request);
        course.setCategory(category);
        course.setInstructor(instructor);
        course.setLanguage(language);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse update(UpdateCourseRequest request) {
        Course course = rules.courseMustExist(request.getId());
        Category category = rules.categoryMustExist(request.getCategoryId());
        rules.categoryMustBeLeaf(request.getCategoryId());
        User instructor = rules.instructorMustExist(request.getInstructorId());
        rules.instructorMustHaveInstructorRole(instructor);
        Language language = rules.languageMustExist(request.getLanguageId());
        rules.priceCannotBeNegative(request.getPrice());

        courseMapper.updateEntityFromRequest(course, request);
        course.setCategory(category);
        course.setInstructor(instructor);
        course.setLanguage(language);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        rules.courseMustExist(id);
        rules.courseCannotBeDeletedWhenReferenced(id);
        courseRepository.deleteById(id);
    }

    @Override
    public CourseResponse getById(Long id) {
        Course course = rules.courseMustExist(id);
        return courseMapper.toResponse(course);
    }

    @Override
    public List<CourseResponse> getAll() {
        return courseMapper.toResponseList(courseRepository.findAll());
    }
}
