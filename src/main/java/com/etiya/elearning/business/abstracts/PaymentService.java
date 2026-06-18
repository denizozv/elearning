package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.responses.PaymentResponse;
import com.etiya.elearning.entities.concretes.Order;
import com.etiya.elearning.entities.enums.PaymentMethod;

public interface PaymentService {

    PaymentResponse createForOrder(Order order, PaymentMethod paymentMethod, String address);

    PaymentResponse getByOrderId(Long orderId);
}
