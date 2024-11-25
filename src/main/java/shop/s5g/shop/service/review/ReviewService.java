package shop.s5g.shop.service.review;

import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.review.ReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;

public interface ReviewService {

    void registerReview(ReviewRequestDto reviewRequestDto, Long customerId);

    PageResponseDto<ReviewResponseDto> getReviewList(Long memberId, Pageable pageable);
}
