package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.responses.PaymentResponse;
import com.etiya.elearning.entities.concretes.Payment;
import org.springframework.stereotype.Component;

/**
 * Payment entity'si ile DTO'ları arasında dönüşüm yapar. Entity asla controller'a
 * sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır.
 */
@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getDate(),
                payment.getAddress()
        );
    }
}
