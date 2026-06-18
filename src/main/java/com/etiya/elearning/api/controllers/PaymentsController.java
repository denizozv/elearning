package com.etiya.elearning.api.controllers;

import com.etiya.elearning.business.abstracts.PaymentService;
import com.etiya.elearning.business.responses.PaymentResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
@Tag(name = "Payments", description = "Ödeme sorgulama (ödeme, checkout sırasında oluşturulur)")
public class PaymentsController {

    private final PaymentService paymentService;

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getByOrderId(orderId));
    }
}
