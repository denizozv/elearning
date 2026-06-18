package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.CreateOrderRequest;
import com.etiya.elearning.business.responses.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(CreateOrderRequest request);

    OrderResponse getById(Long id);

    List<OrderResponse> getByUserId(Long userId);

    List<OrderResponse> getAll();
}
