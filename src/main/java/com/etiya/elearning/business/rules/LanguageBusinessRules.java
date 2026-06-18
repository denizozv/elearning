package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.CourseRepository;
import com.etiya.elearning.dataAccess.abstracts.LanguageRepository;
import com.etiya.elearning.entities.concretes.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Language'e ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class LanguageBusinessRules {

    private final LanguageRepository languageRepository;
    private final CourseRepository courseRepository;

    public Language languageMustExist(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Dil bulunamadı. Id: " + id));
    }

    public void languageNameCannotBeDuplicated(String name) {
        if (languageRepository.existsByLanguageName(name)) {
            throw new BusinessException("Bu isimde bir dil zaten mevcut: " + name);
        }
    }

    public void languageNameCannotBeDuplicatedForUpdate(String name, Long id) {
        if (languageRepository.existsByLanguageNameAndIdNot(name, id)) {
            throw new BusinessException("Bu isimde başka bir dil zaten mevcut: " + name);
        }
    }

    public void languageCannotBeDeletedWhenCoursesExist(Long id) {
        if (courseRepository.existsByLanguage_Id(id)) {
            throw new BusinessException("Bu dile bağlı kurslar olduğu için dil silinemez. Id: " + id);
        }
    }
}
