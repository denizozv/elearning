package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.PaymentRepository;
import com.etiya.elearning.entities.concretes.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Payment'a ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class PaymentBusinessRules {

    private final PaymentRepository paymentRepository;

    public void paymentForOrderMustNotExist(Long orderId) {
        if (paymentRepository.existsByOrder_Id(orderId)) {
            throw new BusinessException("Bu sipariş için ödeme zaten mevcut");
        }
    }

    public Payment paymentMustExistForOrder(Long orderId) {
        return paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new BusinessException("Ödeme bulunamadı"));
    }
}
