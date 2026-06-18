package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.responses.OrderItemResponse;
import com.etiya.elearning.business.responses.OrderResponse;
import com.etiya.elearning.business.responses.PaymentResponse;
import com.etiya.elearning.entities.concretes.Order;
import com.etiya.elearning.entities.concretes.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Order ve OrderItem entity'leri ile DTO'ları arasında dönüşüm yapar. OrderItem'ın
 * bağımsız bir servisi yoktur; Order aggregate'inin parçası olarak burada map'lenir.
 * Entity asla controller'a sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır.
 */
@Component
public class OrderMapper {

    public OrderItemResponse toItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getCourse().getId(),
                orderItem.getCourse().getCourseName(),
                orderItem.getUnitPrice()
        );
    }

    public OrderResponse toResponse(Order order, List<OrderItem> items, PaymentResponse payment) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(this::toItemResponse)
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getTotalPrice(),
                itemResponses,
                payment
        );
    }
}
