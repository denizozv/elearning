package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.CartItemRepository;
import com.etiya.elearning.dataAccess.abstracts.CartRepository;
import com.etiya.elearning.dataAccess.abstracts.OrderRepository;
import com.etiya.elearning.dataAccess.abstracts.UserRepository;
import com.etiya.elearning.entities.concretes.Cart;
import com.etiya.elearning.entities.concretes.CartItem;
import com.etiya.elearning.entities.concretes.Order;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Order'a ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class OrderBusinessRules {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public User userMustExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı. Id: " + userId));
    }

    public Order orderMustExist(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sipariş bulunamadı. Id: " + id));
    }

    public Cart cartMustExist(Long userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BusinessException("Sepet bulunamadı"));
    }

    public List<CartItem> cartMustExistAndNotEmpty(Long userId) {
        Cart cart = cartMustExist(userId);
        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());
        if (items.isEmpty()) {
            throw new BusinessException("Sepet boş, sipariş oluşturulamaz");
        }
        return items;
    }
}
