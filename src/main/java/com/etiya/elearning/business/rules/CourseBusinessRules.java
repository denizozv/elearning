package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.CategoryRepository;
import com.etiya.elearning.dataAccess.abstracts.CourseRepository;
import com.etiya.elearning.dataAccess.abstracts.LanguageRepository;
import com.etiya.elearning.dataAccess.abstracts.OrderItemRepository;
import com.etiya.elearning.dataAccess.abstracts.ReviewRepository;
import com.etiya.elearning.dataAccess.abstracts.UserRepository;
import com.etiya.elearning.entities.concretes.Category;
import com.etiya.elearning.entities.concretes.Course;
import com.etiya.elearning.entities.concretes.Language;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Course'a ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class CourseBusinessRules {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;

    public Course courseMustExist(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Kurs bulunamadı. Id: " + id));
    }

    public Category categoryMustExist(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException("Kategori bulunamadı. Id: " + categoryId));
    }

    public void categoryMustBeLeaf(Long categoryId) {
        if (categoryRepository.existsByParent_Id(categoryId)) {
            throw new BusinessException("Kurs yalnızca en alt (leaf) kategoriye bağlanabilir");
        }
    }

    public User instructorMustExist(Long instructorId) {
        return userRepository.findById(instructorId)
                .orElseThrow(() -> new BusinessException("Eğitmen bulunamadı. Id: " + instructorId));
    }

    public void instructorMustHaveInstructorRole(User instructor) {
        if (instructor.getRole() == null || !"Instructor".equals(instructor.getRole().getName())) {
            throw new BusinessException("Seçilen kullanıcı eğitmen (Instructor) değil");
        }
    }

    public Language languageMustExist(Long languageId) {
        return languageRepository.findById(languageId)
                .orElseThrow(() -> new BusinessException("Dil bulunamadı. Id: " + languageId));
    }

    public void priceCannotBeNegative(BigDecimal price) {
        if (price != null && price.signum() < 0) {
            throw new BusinessException("Fiyat negatif olamaz");
        }
    }

    public void courseCannotBeDeletedWhenReferenced(Long id) {
        if (reviewRepository.existsByCourse_Id(id) || orderItemRepository.existsByCourse_Id(id)) {
            throw new BusinessException("Bu kursa bağlı yorum/sipariş kaydı olduğu için silinemez");
        }
    }
}
