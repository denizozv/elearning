package com.etiya.elearning.business.responses;

import com.etiya.elearning.entities.enums.PaymentMethod;
import com.etiya.elearning.entities.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDateTime date;
    private String address;
}
