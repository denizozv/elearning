package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.CreateLanguageRequest;
import com.etiya.elearning.business.requests.UpdateLanguageRequest;
import com.etiya.elearning.business.responses.LanguageResponse;

import java.util.List;

public interface LanguageService {

    LanguageResponse add(CreateLanguageRequest request);

    LanguageResponse update(UpdateLanguageRequest request);

    void delete(Long id);

    LanguageResponse getById(Long id);

    List<LanguageResponse> getAll();
}
