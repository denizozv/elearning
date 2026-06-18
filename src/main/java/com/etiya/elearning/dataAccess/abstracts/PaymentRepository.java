package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrder_Id(Long orderId);

    Optional<Payment> findByOrder_Id(Long orderId);
}
