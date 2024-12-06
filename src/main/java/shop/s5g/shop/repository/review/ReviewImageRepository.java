package shop.s5g.shop.repository.review;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.review.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReview_ReviewId(Long reviewId);

    void deleteByReview_ReviewId(Long reviewId);
}
