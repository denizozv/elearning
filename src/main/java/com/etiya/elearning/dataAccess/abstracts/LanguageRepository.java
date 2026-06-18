package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    boolean existsByLanguageName(String languageName);

    boolean existsByLanguageNameAndIdNot(String languageName, Long id);
}
