package shop.s5g.shop.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByMemberIdAndActiveTrue(Long memberId, Pageable pageable);

    boolean existsByOrderDetail_id(Long orderDetailId);
}
