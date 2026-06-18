package com.etiya.elearning.business.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private Long roleId;
    private String fullName;
    private String mail;
    private String phone;
    private LocalDate birthDate;
}
