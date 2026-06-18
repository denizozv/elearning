package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCategory_Id(Long categoryId);

    boolean existsByInstructor_Id(Long instructorId);

    boolean existsByLanguage_Id(Long languageId);
}
