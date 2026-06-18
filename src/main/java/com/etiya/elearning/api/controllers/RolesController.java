package com.etiya.elearning.api.controllers;

import com.etiya.elearning.business.abstracts.RoleService;
import com.etiya.elearning.business.requests.CreateRoleRequest;
import com.etiya.elearning.business.requests.UpdateRoleRequest;
import com.etiya.elearning.business.responses.RoleResponse;
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
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
@Tag(name = "Roles", description = "Rol yönetimi")
public class RolesController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> add(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.add(request));
    }

    @PutMapping
    public ResponseEntity<RoleResponse> update(@Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(roleService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }
}
