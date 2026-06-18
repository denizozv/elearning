package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.requests.CreateUserRequest;
import com.etiya.elearning.business.requests.UpdateUserRequest;
import com.etiya.elearning.business.responses.UserResponse;
import com.etiya.elearning.entities.concretes.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User entity'si ile DTO'ları arasında dönüşüm yapar. Entity asla controller'a
 * sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır.
 * Şifre (passwordHash) hiçbir response'a maplenmez; role ve passwordHash manager'da set edilir.
 */
@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setMail(request.getMail());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
        return user;
    }

    public void updateEntityFromRequest(User user, UpdateUserRequest request) {
        user.setFullName(request.getFullName());
        user.setMail(request.getMail());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getRole().getId(),
                user.getFullName(),
                user.getMail(),
                user.getPhone(),
                user.getBirthDate()
        );
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream().map(this::toResponse).toList();
    }
}
