package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.requests.CreateCategoryRequest;
import com.etiya.elearning.business.requests.UpdateCategoryRequest;
import com.etiya.elearning.business.responses.CategoryResponse;
import com.etiya.elearning.entities.concretes.Category;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Category entity'si ile DTO'ları arasında dönüşüm yapar. Entity asla controller'a
 * sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır.
 * parent ilişkisi Manager tarafından set edilir.
 */
@Component
public class CategoryMapper {

    public Category toEntity(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public void updateEntityFromRequest(Category category, UpdateCategoryRequest request) {
        category.setName(request.getName());
    }

    public CategoryResponse toResponse(Category category) {
        Long parentId = category.getParent() != null ? category.getParent().getId() : null;
        return new CategoryResponse(category.getId(), category.getName(), parentId);
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream().map(this::toResponse).toList();
    }
}
