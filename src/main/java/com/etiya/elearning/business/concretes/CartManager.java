package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.CartService;
import com.etiya.elearning.business.mappers.CartMapper;
import com.etiya.elearning.business.requests.AddToCartRequest;
import com.etiya.elearning.business.responses.CartResponse;
import com.etiya.elearning.business.rules.CartBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.CartItemRepository;
import com.etiya.elearning.dataAccess.abstracts.CartRepository;
import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.entities.concretes.Cart;
import com.etiya.elearning.entities.concretes.CartItem;
import com.etiya.elearning.entities.concretes.Course;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CartManager implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartBusinessRules rules;

    @Override
    @Transactional
    public CartResponse addCourseToCart(AddToCartRequest request) {
        User user = rules.userMustExist(request.getUserId());
        Course course = rules.courseMustExist(request.getCourseId());
        rules.courseNotAlreadyPurchased(request.getUserId(), request.getCourseId());
        rules.userCannotAddOwnCourse(request.getUserId(), course);

        // Sepet, ilk kurs eklendiğinde (lazy) oluşturulur.
        Cart cart = cartRepository.findByUser_Id(request.getUserId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepository.save(c);
                });

        rules.courseNotAlreadyInCart(cart.getId(), request.getCourseId());

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setCourse(course);
        cartItemRepository.save(item);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeCourseFromCart(Long userId, Long courseId) {
        Cart cart = rules.cartMustExistForUser(userId);
        CartItem item = cartItemRepository.findByCart_IdAndCourse_Id(cart.getId(), courseId)
                .orElseThrow(() -> new BusinessException("Kurs sepette bulunamadı"));
        cartItemRepository.delete(item);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = rules.cartMustExistForUser(userId);
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = rules.cartMustExistForUser(userId);
        cartItemRepository.deleteByCart_Id(cart.getId());
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());
        return cartMapper.toResponse(cart, items);
    }
}
