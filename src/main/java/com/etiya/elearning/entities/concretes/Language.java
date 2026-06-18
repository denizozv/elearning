package com.etiya.elearning.entities.concretes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "languages")
public class Language extends BaseEntity {

    @Column(name = "language_name", nullable = false, unique = true, length = 50)
    private String languageName;

    @OneToMany(mappedBy = "language")
    private List<Course> courses;
}
