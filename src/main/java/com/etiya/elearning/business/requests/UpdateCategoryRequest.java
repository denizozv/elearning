package com.etiya.elearning.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {

    @NotNull(message = "Id boş olamaz")
    private Long id;

    @NotBlank(message = "Kategori adı boş olamaz")
    @Size(min = 2, max = 100, message = "Kategori adı 2-100 karakter olmalıdır")
    private String name;

    // Nullable: null ise kök (root) kategoridir.
    private Long parentId;
}
