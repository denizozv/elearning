package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    boolean existsByCart_IdAndCourse_Id(Long cartId, Long courseId);

    Optional<CartItem> findByCart_IdAndCourse_Id(Long cartId, Long courseId);

    List<CartItem> findByCart_Id(Long cartId);

    void deleteByCart_Id(Long cartId);
}
