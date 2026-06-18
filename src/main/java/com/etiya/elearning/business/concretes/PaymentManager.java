package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.PaymentService;
import com.etiya.elearning.business.mappers.PaymentMapper;
import com.etiya.elearning.business.responses.PaymentResponse;
import com.etiya.elearning.business.rules.PaymentBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.PaymentRepository;
import com.etiya.elearning.entities.concretes.Order;
import com.etiya.elearning.entities.concretes.Payment;
import com.etiya.elearning.entities.enums.PaymentMethod;
import com.etiya.elearning.entities.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentBusinessRules rules;

    @Override
    public PaymentResponse createForOrder(Order order, PaymentMethod paymentMethod, String address) {
        rules.paymentForOrderMustNotExist(order.getId());
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setAddress(address);
        payment.setDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getByOrderId(Long orderId) {
        Payment payment = rules.paymentMustExistForOrder(orderId);
        return paymentMapper.toResponse(payment);
    }
}
