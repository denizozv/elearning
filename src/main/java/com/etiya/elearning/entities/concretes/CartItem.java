package com.etiya.elearning.entities.concretes;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "cart_items",
        // İş kuralı: aynı sepette aynı kurs iki kez bulunamaz.
        uniqueConstraints = @UniqueConstraint(name = "uk_cart_course", columnNames = {"cart_id", "course_id"})
)
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Not: quantity ve unitPrice YOK. Fiyat checkout anında Course'tan OrderItem'a snapshot edilir.
}
