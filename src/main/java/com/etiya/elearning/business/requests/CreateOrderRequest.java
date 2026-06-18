package com.etiya.elearning.business.requests;

import com.etiya.elearning.entities.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "Kullanıcı id boş olamaz")
    private Long userId;

    @NotNull(message = "Ödeme yöntemi boş olamaz")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "Adres boş olamaz")
    private String address;
}
