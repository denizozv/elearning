package com.etiya.elearning.api.controllers;

import com.etiya.elearning.business.abstracts.CourseService;
import com.etiya.elearning.business.requests.CreateCourseRequest;
import com.etiya.elearning.business.requests.UpdateCourseRequest;
import com.etiya.elearning.business.responses.CourseResponse;
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
@RequestMapping("/api/v1/courses")
@AllArgsConstructor
@Tag(name = "Courses", description = "Kurs yönetimi")
public class CoursesController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> add(@Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.add(request));
    }

    @PutMapping
    public ResponseEntity<CourseResponse> update(@Valid @RequestBody UpdateCourseRequest request) {
        return ResponseEntity.ok(courseService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }
}
