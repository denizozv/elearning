package com.etiya.elearning.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateLanguageRequest {

    @NotBlank(message = "Dil adı boş olamaz")
    @Size(min = 2, max = 50, message = "Dil adı 2-50 karakter olmalıdır")
    private String languageName;
}
