package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.AddToCartRequest;
import com.etiya.elearning.business.responses.CartResponse;

public interface CartService {

    CartResponse addCourseToCart(AddToCartRequest request);

    CartResponse removeCourseFromCart(Long userId, Long courseId);

    CartResponse getCartByUserId(Long userId);

    void clearCart(Long userId);
}
