package com.etiya.elearning.api.controllers;

import com.etiya.elearning.business.abstracts.ReviewService;
import com.etiya.elearning.business.requests.CreateReviewRequest;
import com.etiya.elearning.business.requests.UpdateReviewRequest;
import com.etiya.elearning.business.responses.ReviewResponse;
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
@RequestMapping("/api/v1/reviews")
@AllArgsConstructor
@Tag(name = "Reviews", description = "Kurs yorumları")
public class ReviewsController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> add(@Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.add(request));
    }

    @PutMapping
    public ResponseEntity<ReviewResponse> update(@Valid @RequestBody UpdateReviewRequest request) {
        return ResponseEntity.ok(reviewService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }
}
