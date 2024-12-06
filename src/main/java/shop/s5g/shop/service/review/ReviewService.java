package shop.s5g.shop.service.review;

import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.review.CreateReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;
import shop.s5g.shop.dto.review.UpdateReviewRequestDto;

public interface ReviewService {

    void registerReview(CreateReviewRequestDto reviewRequestDto, Long customerId);

    PageResponseDto<ReviewResponseDto> getReviewList(Long memberId, Pageable pageable);

    void updateReview(UpdateReviewRequestDto updateReviewRequestDto, Long customerId);
}
