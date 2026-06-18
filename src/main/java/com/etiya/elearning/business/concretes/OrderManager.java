package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.OrderService;
import com.etiya.elearning.business.abstracts.PaymentService;
import com.etiya.elearning.business.mappers.OrderMapper;
import com.etiya.elearning.business.requests.CreateOrderRequest;
import com.etiya.elearning.business.responses.OrderResponse;
import com.etiya.elearning.business.responses.PaymentResponse;
import com.etiya.elearning.business.rules.OrderBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.CartItemRepository;
import com.etiya.elearning.dataAccess.abstracts.CartRepository;
import com.etiya.elearning.dataAccess.abstracts.OrderItemRepository;
import com.etiya.elearning.dataAccess.abstracts.OrderRepository;
import com.etiya.elearning.entities.concretes.Cart;
import com.etiya.elearning.entities.concretes.CartItem;
import com.etiya.elearning.entities.concretes.Order;
import com.etiya.elearning.entities.concretes.OrderItem;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderManager implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderBusinessRules rules;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public OrderResponse checkout(CreateOrderRequest request) {
        User user = rules.userMustExist(request.getUserId());
        List<CartItem> cartItems = rules.cartMustExistAndNotEmpty(request.getUserId());

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setCourse(cartItem.getCourse());
            orderItem.setUnitPrice(cartItem.getCourse().getPrice());
            orderItems.add(orderItem);
            total = total.add(cartItem.getCourse().getPrice());
        }
        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        PaymentResponse paymentResponse = paymentService.createForOrder(
                savedOrder, request.getPaymentMethod(), request.getAddress());

        // Sipariş oluştuğuna göre sepeti temizle.
        Cart cart = cartRepository.findByUser_Id(request.getUserId()).orElseThrow();
        cartItemRepository.deleteByCart_Id(cart.getId());

        return orderMapper.toResponse(savedOrder, orderItems, paymentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        Order order = rules.orderMustExist(id);
        return buildResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getByUserId(Long userId) {
        return orderRepository.findByUser_Id(userId).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(this::buildResponse)
                .toList();
    }

    private OrderResponse buildResponse(Order order) {
        PaymentResponse paymentResponse = paymentService.getByOrderId(order.getId());
        return orderMapper.toResponse(order, order.getOrderItems(), paymentResponse);
    }
}
