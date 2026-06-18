package com.etiya.elearning.api.controllers;

import com.etiya.elearning.business.abstracts.CategoryService;
import com.etiya.elearning.business.requests.CreateCategoryRequest;
import com.etiya.elearning.business.requests.UpdateCategoryRequest;
import com.etiya.elearning.business.responses.CategoryResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
@Tag(name = "Categories", description = "Kategori yönetimi (çok seviyeli ağaç)")
public class CategoriesController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> add(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.add(request));
    }

    @PutMapping
    public ResponseEntity<CategoryResponse> update(@Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}
