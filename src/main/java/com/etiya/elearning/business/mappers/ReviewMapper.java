package com.etiya.elearning.business.mappers;

import com.etiya.elearning.business.requests.CreateReviewRequest;
import com.etiya.elearning.business.requests.UpdateReviewRequest;
import com.etiya.elearning.business.responses.ReviewResponse;
import com.etiya.elearning.entities.concretes.Review;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Review entity'si ile DTO'ları arasında dönüşüm yapar. Entity asla controller'a
 * sızdırılmaz; dönüşümler bu business katmanı bileşeninde yapılır. course ve user
 * ilişkileri manager tarafından set edilir.
 */
@Component
public class ReviewMapper {

    public Review toEntity(CreateReviewRequest request) {
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return review;
    }

    public void updateEntityFromRequest(Review review, UpdateReviewRequest request) {
        review.setRating(request.getRating());
        review.setComment(request.getComment());
    }

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getCourse().getId(),
                review.getUser().getId(),
                review.getRating(),
                review.getComment());
    }

    public List<ReviewResponse> toResponseList(List<Review> reviews) {
        return reviews.stream().map(this::toResponse).toList();
    }
}
