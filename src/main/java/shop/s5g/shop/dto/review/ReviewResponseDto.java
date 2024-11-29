package shop.s5g.shop.dto.review;

import java.time.LocalDateTime;
import java.util.List;
import shop.s5g.shop.entity.review.Review;
import shop.s5g.shop.entity.review.ReviewImage;

public record ReviewResponseDto(
    Long bookId,
    String bookTitle,
    String memberLoginId,
    Long reviewId,
    int score,
    String content,
    LocalDateTime reviewAt,
    List<String> imagePathList
) {

    public static ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(review.getBook().getBookId(), review.getBook().getTitle(),
            review.getMember().getLoginId(), review.getReviewId(), review.getScore(),
            review.getContent(), review.getReviewAt(),
            review.getReviewImages().stream().map(ReviewImage::getImageName).toList());
    }
}
