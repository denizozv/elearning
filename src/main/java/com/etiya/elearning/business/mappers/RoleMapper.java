package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.requests.CreateRoleRequest;
import com.etiya.elearning.business.requests.UpdateRoleRequest;
import com.etiya.elearning.business.responses.RoleResponse;
import com.etiya.elearning.entities.concretes.Role;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Role entity'si ile DTO'ları arasında dönüşüm yapar. Entity asla controller'a
 * sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır.
 */
@Component
public class RoleMapper {

    public Role toEntity(CreateRoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        return role;
    }

    public void updateEntityFromRequest(Role role, UpdateRoleRequest request) {
        role.setName(request.getName());
    }

    public RoleResponse toResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName());
    }

    public List<RoleResponse> toResponseList(List<Role> roles) {
        return roles.stream().map(this::toResponse).toList();
    }
}
