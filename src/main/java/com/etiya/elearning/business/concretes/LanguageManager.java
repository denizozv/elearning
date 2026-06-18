package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.LanguageService;
import com.etiya.elearning.business.mappers.LanguageMapper;
import com.etiya.elearning.business.requests.CreateLanguageRequest;
import com.etiya.elearning.business.requests.UpdateLanguageRequest;
import com.etiya.elearning.business.responses.LanguageResponse;
import com.etiya.elearning.business.rules.LanguageBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.LanguageRepository;
import com.etiya.elearning.entities.concretes.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LanguageManager implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;
    private final LanguageBusinessRules rules;

    @Override
    public LanguageResponse add(CreateLanguageRequest request) {
        rules.languageNameCannotBeDuplicated(request.getLanguageName());
        Language language = languageMapper.toEntity(request);
        return languageMapper.toResponse(languageRepository.save(language));
    }

    @Override
    public LanguageResponse update(UpdateLanguageRequest request) {
        Language language = rules.languageMustExist(request.getId());
        rules.languageNameCannotBeDuplicatedForUpdate(request.getLanguageName(), request.getId());
        languageMapper.updateEntityFromRequest(language, request);
        return languageMapper.toResponse(languageRepository.save(language));
    }

    @Override
    public void delete(Long id) {
        rules.languageMustExist(id);
        rules.languageCannotBeDeletedWhenCoursesExist(id);
        languageRepository.deleteById(id);
    }

    @Override
    public LanguageResponse getById(Long id) {
        Language language = rules.languageMustExist(id);
        return languageMapper.toResponse(language);
    }

    @Override
    public List<LanguageResponse> getAll() {
        return languageMapper.toResponseList(languageRepository.findAll());
    }
}
