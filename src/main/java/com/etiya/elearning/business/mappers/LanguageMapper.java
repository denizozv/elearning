package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.requests.CreateLanguageRequest;
import com.etiya.elearning.business.requests.UpdateLanguageRequest;
import com.etiya.elearning.business.responses.LanguageResponse;
import com.etiya.elearning.entities.concretes.Language;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Language entity'si ile DTO'ları arasında dönüşüm yapar. Entity asla controller'a
 * sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır.
 */
@Component
public class LanguageMapper {

    public Language toEntity(CreateLanguageRequest request) {
        Language language = new Language();
        language.setLanguageName(request.getLanguageName());
        return language;
    }

    public void updateEntityFromRequest(Language language, UpdateLanguageRequest request) {
        language.setLanguageName(request.getLanguageName());
    }

    public LanguageResponse toResponse(Language language) {
        return new LanguageResponse(language.getId(), language.getLanguageName());
    }

    public List<LanguageResponse> toResponseList(List<Language> languages) {
        return languages.stream().map(this::toResponse).toList();
    }
}
