package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.RoleService;
import com.etiya.elearning.business.mappers.RoleMapper;
import com.etiya.elearning.business.requests.CreateRoleRequest;
import com.etiya.elearning.business.requests.UpdateRoleRequest;
import com.etiya.elearning.business.responses.RoleResponse;
import com.etiya.elearning.business.rules.RoleBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.RoleRepository;
import com.etiya.elearning.entities.concretes.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleManager implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleBusinessRules rules;

    @Override
    public RoleResponse add(CreateRoleRequest request) {
        rules.roleNameCannotBeDuplicated(request.getName());
        Role role = roleMapper.toEntity(request);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse update(UpdateRoleRequest request) {
        Role role = rules.roleMustExist(request.getId());
        rules.roleNameCannotBeDuplicatedForUpdate(request.getName(), request.getId());
        roleMapper.updateEntityFromRequest(role, request);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public void delete(Long id) {
        rules.roleMustExist(id);
        rules.roleCannotBeDeletedWhenUsersExist(id);
        roleRepository.deleteById(id);
    }

    @Override
    public RoleResponse getById(Long id) {
        Role role = rules.roleMustExist(id);
        return roleMapper.toResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleMapper.toResponseList(roleRepository.findAll());
    }
}
