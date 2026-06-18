package com.etiya.elearning.business.concretes;

import com.etiya.elearning.business.abstracts.ReviewService;
import com.etiya.elearning.business.mappers.ReviewMapper;
import com.etiya.elearning.business.requests.CreateReviewRequest;
import com.etiya.elearning.business.requests.UpdateReviewRequest;
import com.etiya.elearning.business.responses.ReviewResponse;
import com.etiya.elearning.business.rules.ReviewBusinessRules;
import com.etiya.elearning.dataAccess.abstracts.ReviewRepository;
import com.etiya.elearning.entities.concretes.Course;
import com.etiya.elearning.entities.concretes.Review;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewManager implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewBusinessRules rules;

    @Override
    public ReviewResponse add(CreateReviewRequest request) {
        Course course = rules.courseMustExist(request.getCourseId());
        User user = rules.userMustExist(request.getUserId());
        rules.ratingMustBeBetween1And5(request.getRating());
        rules.userMustHavePurchasedCourse(request.getUserId(), request.getCourseId());
        rules.userCannotReviewSameCourseTwice(request.getUserId(), request.getCourseId());
        Review review = reviewMapper.toEntity(request);
        review.setCourse(course);
        review.setUser(user);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse update(UpdateReviewRequest request) {
        Review review = rules.reviewMustExist(request.getId());
        rules.ratingMustBeBetween1And5(request.getRating());
        reviewMapper.updateEntityFromRequest(review, request);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        rules.reviewMustExist(id);
        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewResponse getById(Long id) {
        Review review = rules.reviewMustExist(id);
        return reviewMapper.toResponse(review);
    }

    @Override
    public List<ReviewResponse> getAll() {
        return reviewMapper.toResponseList(reviewRepository.findAll());
    }
}
