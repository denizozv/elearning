package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.CategoryService;
import com.etiya.elearning.business.mappers.CategoryMapper;
import com.etiya.elearning.business.requests.CreateCategoryRequest;
import com.etiya.elearning.business.requests.UpdateCategoryRequest;
import com.etiya.elearning.business.responses.CategoryResponse;
import com.etiya.elearning.business.rules.CategoryBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.CategoryRepository;
import com.etiya.elearning.entities.concretes.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryManager implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryBusinessRules rules;

    @Override
    public CategoryResponse add(CreateCategoryRequest request) {
        Category parent = rules.parentMustExistIfProvided(request.getParentId());
        rules.nameCannotBeDuplicatedUnderSameParent(request.getName(), request.getParentId());
        Category category = categoryMapper.toEntity(request);
        category.setParent(parent);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(UpdateCategoryRequest request) {
        Category category = rules.categoryMustExist(request.getId());
        rules.parentCannotBeItself(request.getId(), request.getParentId());
        Category parent = rules.parentMustExistIfProvided(request.getParentId());
        rules.parentCannotCreateCycle(request.getId(), request.getParentId());
        rules.nameCannotBeDuplicatedUnderSameParentForUpdate(request.getName(), request.getParentId(), request.getId());
        categoryMapper.updateEntityFromRequest(category, request);
        category.setParent(parent);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        rules.categoryMustExist(id);
        rules.categoryCannotBeDeletedWhenHasChildren(id);
        rules.categoryCannotBeDeletedWhenCoursesExist(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = rules.categoryMustExist(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }
}
