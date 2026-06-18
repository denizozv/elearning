package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.CreateCategoryRequest;
import com.etiya.elearning.business.requests.UpdateCategoryRequest;
import com.etiya.elearning.business.responses.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse add(CreateCategoryRequest request);

    CategoryResponse update(UpdateCategoryRequest request);

    void delete(Long id);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();
}
