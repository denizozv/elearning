package com.etiya.elearning.business.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotNull(message = "Rol id boş olamaz")
    private Long roleId;

    @NotBlank(message = "Ad soyad boş olamaz")
    @Size(min = 2, max = 100, message = "Ad soyad 2-100 karakter olmalıdır")
    private String fullName;

    @NotBlank(message = "Mail boş olamaz")
    @Email(message = "Geçerli bir mail adresi giriniz")
    private String mail;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 8, max = 100, message = "Şifre 8-100 karakter olmalıdır")
    private String password;

    private String phone;

    @Past(message = "Doğum tarihi geçmişte olmalıdır")
    private LocalDate birthDate;
}
