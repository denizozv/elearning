package com.etiya.elearning.entities.concretes;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Tüm entity'lerin türediği temel sınıf. Ortak kimlik ve denetim (audit)
 * alanlarını barındırır. {@code @MappedSuperclass} olduğu için kendi tablosu
 * yoktur; alanları, türeyen her entity'nin tablosuna kolon olarak işlenir.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // Audit alanlarını JPA yaşam döngüsünde otomatik doldurur; manager'lar elle set etmez.
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
