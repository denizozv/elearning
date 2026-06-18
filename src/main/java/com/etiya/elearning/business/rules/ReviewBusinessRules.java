package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.CourseRepository;
import com.etiya.elearning.dataAccess.abstracts.OrderItemRepository;
import com.etiya.elearning.dataAccess.abstracts.ReviewRepository;
import com.etiya.elearning.dataAccess.abstracts.UserRepository;
import com.etiya.elearning.entities.concretes.Course;
import com.etiya.elearning.entities.concretes.Review;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Review'e ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class ReviewBusinessRules {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public Review reviewMustExist(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Yorum bulunamadı. Id: " + id));
    }

    public Course courseMustExist(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException("Kurs bulunamadı. Id: " + courseId));
    }

    public User userMustExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı. Id: " + userId));
    }

    public void ratingMustBeBetween1And5(int rating) {
        if (rating < 1 || rating > 5) {
            throw new BusinessException("Puan 1 ile 5 arasında olmalıdır");
        }
    }

    public void userCannotReviewSameCourseTwice(Long userId, Long courseId) {
        if (reviewRepository.existsByUser_IdAndCourse_Id(userId, courseId)) {
            throw new BusinessException("Bu kursa zaten yorum yaptınız");
        }
    }

    public void userMustHavePurchasedCourse(Long userId, Long courseId) {
        if (!orderItemRepository.existsByOrder_User_IdAndCourse_Id(userId, courseId)) {
            throw new BusinessException("Yalnızca satın aldığınız kursa yorum yapabilirsiniz");
        }
    }
}
