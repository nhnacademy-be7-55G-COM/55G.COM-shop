package shop.s5g.shop.controller.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.review.CreateReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;
import shop.s5g.shop.dto.review.UpdateReviewRequestDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.review.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<MessageDto> registerReview(
        @RequestBody CreateReviewRequestDto reviewRequestDto, BindingResult bindingResult,
        @AuthenticationPrincipal ShopMemberDetail detail) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        reviewService.registerReview(reviewRequestDto, detail.getCustomerId());

        return ResponseEntity.status(HttpStatusCode.valueOf(201))
            .body(new MessageDto("리뷰가 등록되었습니다."));
    }

    @GetMapping("/list")
    public PageResponseDto<ReviewResponseDto> getReviewList(Pageable pageable,
        @AuthenticationPrincipal ShopMemberDetail detail) {

        return reviewService.getReviewList(detail.getCustomerId(), pageable);
    }

    @PatchMapping
    public ResponseEntity<MessageDto> updateReview(
        @Valid @RequestBody UpdateReviewRequestDto updateReviewRequestDto,
        BindingResult bindingResult, @AuthenticationPrincipal ShopMemberDetail detail) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        reviewService.updateReview(updateReviewRequestDto, detail.getCustomerId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageDto("리뷰가 수정되었습니다."));
    }
}
