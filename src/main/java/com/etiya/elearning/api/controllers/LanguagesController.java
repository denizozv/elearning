package com.etiya.elearning.api.controllers;

import com.etiya.elearning.business.abstracts.LanguageService;
import com.etiya.elearning.business.requests.CreateLanguageRequest;
import com.etiya.elearning.business.requests.UpdateLanguageRequest;
import com.etiya.elearning.business.responses.LanguageResponse;
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
@RequestMapping("/api/v1/languages")
@AllArgsConstructor
@Tag(name = "Languages", description = "Dil yönetimi")
public class LanguagesController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageResponse> add(@Valid @RequestBody CreateLanguageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.add(request));
    }

    @PutMapping
    public ResponseEntity<LanguageResponse> update(@Valid @RequestBody UpdateLanguageRequest request) {
        return ResponseEntity.ok(languageService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAll() {
        return ResponseEntity.ok(languageService.getAll());
    }
}
