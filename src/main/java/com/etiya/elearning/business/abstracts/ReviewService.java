package com.etiya.elearning.business.abstracts;

import com.etiya.elearning.business.requests.CreateReviewRequest;
import com.etiya.elearning.business.requests.UpdateReviewRequest;
import com.etiya.elearning.business.responses.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse add(CreateReviewRequest request);

    ReviewResponse update(UpdateReviewRequest request);

    void delete(Long id);

    ReviewResponse getById(Long id);

    List<ReviewResponse> getAll();
}
