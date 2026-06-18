package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);
}
