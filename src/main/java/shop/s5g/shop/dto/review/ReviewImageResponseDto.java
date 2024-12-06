package shop.s5g.shop.dto.review;

import shop.s5g.shop.entity.review.ReviewImage;

public record ReviewImageResponseDto(
    String imageName
) {

    public static ReviewImageResponseDto from(ReviewImage reviewImage) {
        return new ReviewImageResponseDto(reviewImage.getImageName());
    }
}
