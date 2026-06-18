package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.CartItemRepository;
import com.etiya.elearning.dataAccess.abstracts.CartRepository;
import com.etiya.elearning.dataAccess.abstracts.CourseRepository;
import com.etiya.elearning.dataAccess.abstracts.OrderItemRepository;
import com.etiya.elearning.dataAccess.abstracts.UserRepository;
import com.etiya.elearning.entities.concretes.Cart;
import com.etiya.elearning.entities.concretes.Course;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Cart akışına ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class CartBusinessRules {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public User userMustExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı. Id: " + userId));
    }

    public Course courseMustExist(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException("Kurs bulunamadı. Id: " + courseId));
    }

    public Cart cartMustExistForUser(Long userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BusinessException("Kullanıcının sepeti bulunamadı"));
    }

    public void courseNotAlreadyInCart(Long cartId, Long courseId) {
        if (cartItemRepository.existsByCart_IdAndCourse_Id(cartId, courseId)) {
            throw new BusinessException("Bu kurs zaten sepetinizde");
        }
    }

    public void courseNotAlreadyPurchased(Long userId, Long courseId) {
        if (orderItemRepository.existsByOrder_User_IdAndCourse_Id(userId, courseId)) {
            throw new BusinessException("Bu kursu zaten satın aldınız");
        }
    }

    public void userCannotAddOwnCourse(Long userId, Course course) {
        if (course.getInstructor() != null && course.getInstructor().getId().equals(userId)) {
            throw new BusinessException("Kendi eğitmeni olduğunuz kursu sepete ekleyemezsiniz");
        }
    }
}
