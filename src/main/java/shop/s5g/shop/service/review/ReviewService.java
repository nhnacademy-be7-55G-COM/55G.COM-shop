package shop.s5g.shop.service.review;

import shop.s5g.shop.dto.review.CreateReviewRequestDto;

public interface ReviewService {

    void registerReview(CreateReviewRequestDto createReviewRequestDto, Long customerId);
}
