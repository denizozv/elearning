package com.etiya.elearning.entities.concretes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Self-referencing: üst kategori. Kök kategorilerde null'dur.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Self-referencing: alt kategoriler (çok seviyeli ağaç).
    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    // Bu kategoriye doğrudan bağlı kurslar (yalnızca leaf kategorilerde dolu olur).
    @OneToMany(mappedBy = "category")
    private List<Course> courses;
}
