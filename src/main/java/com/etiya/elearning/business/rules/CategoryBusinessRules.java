package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.CategoryRepository;
import com.etiya.elearning.dataAccess.abstracts.CourseRepository;
import com.etiya.elearning.entities.concretes.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Category'e ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class CategoryBusinessRules {

    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    public Category categoryMustExist(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Kategori bulunamadı. Id: " + id));
    }

    public Category parentMustExistIfProvided(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new BusinessException("Üst kategori bulunamadı"));
    }

    public void parentCannotBeItself(Long id, Long parentId) {
        if (parentId != null && parentId.equals(id)) {
            throw new BusinessException("Bir kategori kendi üst kategorisi olamaz");
        }
    }

    public void nameCannotBeDuplicatedUnderSameParent(String name, Long parentId) {
        boolean exists = parentId == null
                ? categoryRepository.existsByNameAndParentIsNull(name)
                : categoryRepository.existsByNameAndParent_Id(name, parentId);
        if (exists) {
            throw new BusinessException("Bu isimde bir kategori aynı üst kategori altında zaten mevcut: " + name);
        }
    }

    public void nameCannotBeDuplicatedUnderSameParentForUpdate(String name, Long parentId, Long id) {
        boolean exists = parentId == null
                ? categoryRepository.existsByNameAndParentIsNullAndIdNot(name, id)
                : categoryRepository.existsByNameAndParent_IdAndIdNot(name, parentId, id);
        if (exists) {
            throw new BusinessException("Bu isimde başka bir kategori aynı üst kategori altında zaten mevcut: " + name);
        }
    }

    public void parentCannotCreateCycle(Long id, Long parentId) {
        Long current = parentId;
        while (current != null) {
            if (current.equals(id)) {
                throw new BusinessException("Kategori döngüsü oluşturulamaz");
            }
            Category c = categoryRepository.findById(current).orElse(null);
            current = (c != null && c.getParent() != null) ? c.getParent().getId() : null;
        }
    }

    public void categoryCannotBeDeletedWhenHasChildren(Long id) {
        if (categoryRepository.existsByParent_Id(id)) {
            throw new BusinessException("Altında alt kategorileri olduğu için kategori silinemez. Id: " + id);
        }
    }

    public void categoryCannotBeDeletedWhenCoursesExist(Long id) {
        if (courseRepository.existsByCategory_Id(id)) {
            throw new BusinessException("Bu kategoriye bağlı kurslar olduğu için kategori silinemez. Id: " + id);
        }
    }
}
