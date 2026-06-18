package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Bir kullanıcı aynı kursa yalnızca bir yorum yapabilir.
    boolean existsByUser_IdAndCourse_Id(Long userId, Long courseId);

    boolean existsByCourse_Id(Long courseId);
}
