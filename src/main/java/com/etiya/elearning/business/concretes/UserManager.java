package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.UserService;
import com.etiya.elearning.business.mappers.UserMapper;
import com.etiya.elearning.business.requests.CreateUserRequest;
import com.etiya.elearning.business.requests.UpdateUserRequest;
import com.etiya.elearning.business.responses.UserResponse;
import com.etiya.elearning.business.rules.UserBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.UserRepository;
import com.etiya.elearning.entities.concretes.Role;
import com.etiya.elearning.entities.concretes.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserBusinessRules rules;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponse add(CreateUserRequest request) {
        rules.mailCannotBeDuplicated(request.getMail());
        Role role = rules.roleMustExist(request.getRoleId());
        User user = userMapper.toEntity(request);
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse update(UpdateUserRequest request) {
        User user = rules.userMustExist(request.getId());
        rules.mailCannotBeDuplicatedForUpdate(request.getMail(), request.getId());
        Role role = rules.roleMustExist(request.getRoleId());
        userMapper.updateEntityFromRequest(user, request);
        user.setRole(role);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        rules.userMustExist(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = rules.userMustExist(id);
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userMapper.toResponseList(userRepository.findAll());
    }
}
