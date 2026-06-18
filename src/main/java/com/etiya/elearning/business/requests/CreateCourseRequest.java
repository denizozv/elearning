package com.etiya.elearning.business.requests;

import com.etiya.elearning.entities.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

    @NotNull(message = "Kategori boş olamaz")
    private Long categoryId;

    @NotNull(message = "Eğitmen boş olamaz")
    private Long instructorId;

    @NotNull(message = "Dil boş olamaz")
    private Long languageId;

    @NotBlank(message = "Kurs adı boş olamaz")
    @Size(min = 3, max = 150, message = "Kurs adı 3-150 karakter olmalıdır")
    private String courseName;

    @NotNull(message = "Fiyat boş olamaz")
    @PositiveOrZero(message = "Fiyat negatif olamaz")
    private BigDecimal price;

    @Size(max = 2000, message = "Açıklama en fazla 2000 karakter olabilir")
    private String description;

    @NotNull(message = "Zorluk seviyesi boş olamaz")
    private Difficulty difficulty;
}
