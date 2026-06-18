package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Aynı parent altında isim benzersizliği (kardeşler arası çakışma kontrolü).
    boolean existsByNameAndParent_Id(String name, Long parentId);

    boolean existsByNameAndParentIsNull(String name);

    boolean existsByNameAndParent_IdAndIdNot(String name, Long parentId, Long id);

    boolean existsByNameAndParentIsNullAndIdNot(String name, Long id);

    // Bu kategorinin çocuğu var mı? (leaf kontrolü için: çocuğu yoksa leaf'tir)
    boolean existsByParent_Id(Long parentId);
}
