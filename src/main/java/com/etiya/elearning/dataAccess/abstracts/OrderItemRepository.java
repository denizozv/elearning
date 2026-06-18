package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Kullanıcı bu kursu daha önce satın almış mı? (Order -> User zinciri üzerinden)
    boolean existsByOrder_User_IdAndCourse_Id(Long userId, Long courseId);

    boolean existsByCourse_Id(Long courseId);
}
