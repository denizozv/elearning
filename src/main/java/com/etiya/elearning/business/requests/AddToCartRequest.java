package com.etiya.elearning.business.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    @NotNull(message = "Kullanıcı id boş olamaz")
    private Long userId;

    @NotNull(message = "Kurs id boş olamaz")
    private Long courseId;
}
