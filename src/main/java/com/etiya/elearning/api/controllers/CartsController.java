package com.etiya.elearning.api.controllers;

import com.etiya.elearning.business.abstracts.CartService;
import com.etiya.elearning.business.requests.AddToCartRequest;
import com.etiya.elearning.business.responses.CartResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@AllArgsConstructor
@Tag(name = "Carts", description = "Sepet işlemleri (kullanıcı başına tek sepet)")
public class CartsController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addCourseToCart(@Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addCourseToCart(request));
    }

    @DeleteMapping("/users/{userId}/courses/{courseId}")
    public ResponseEntity<CartResponse> removeCourseFromCart(@PathVariable Long userId, @PathVariable Long courseId) {
        return ResponseEntity.ok(cartService.removeCourseFromCart(userId, courseId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
