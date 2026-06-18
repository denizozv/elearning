package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.CreateRoleRequest;
import com.etiya.elearning.business.requests.UpdateRoleRequest;
import com.etiya.elearning.business.responses.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse add(CreateRoleRequest request);

    RoleResponse update(UpdateRoleRequest request);

    void delete(Long id);

    RoleResponse getById(Long id);

    List<RoleResponse> getAll();
}
