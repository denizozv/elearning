package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.responses.CartItemResponse;
import com.etiya.elearning.business.responses.CartResponse;
import com.etiya.elearning.entities.concretes.Cart;
import com.etiya.elearning.entities.concretes.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cart aggregate'i ile DTO'ları arasında dönüşüm yapar. Entity asla controller'a
 * sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır.
 */
@Component
public class CartMapper {

    public CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getCourse().getId(),
                item.getCourse().getCourseName(),
                item.getCourse().getPrice()
        );
    }

    public CartResponse toResponse(Cart cart, List<CartItem> items) {
        List<CartItemResponse> itemResponses = items.stream()
                .map(this::toItemResponse)
                .toList();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : items) {
            totalPrice = totalPrice.add(item.getCourse().getPrice());
        }

        return new CartResponse(cart.getId(), cart.getUser().getId(), itemResponses, totalPrice);
    }
}
