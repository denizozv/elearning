package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.CreateUserRequest;
import com.etiya.elearning.business.requests.UpdateUserRequest;
import com.etiya.elearning.business.responses.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse add(CreateUserRequest request);

    UserResponse update(UpdateUserRequest request);

    void delete(Long id);

    UserResponse getById(Long id);

    List<UserResponse> getAll();
}
