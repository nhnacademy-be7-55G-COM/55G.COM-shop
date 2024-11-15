package shop.s5g.shop.repository.coupon.book.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.book.CouponBookDetailsForBookDto;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;

public interface CouponBookQuerydslRepository {

    // Check ( 해당 책에 해당 쿠폰 템플릿이 적용 되었는지 확인 )
    boolean existsByBookAndCouponTemplate(Long bookId, Long couponTemplateId);

    // Read
    CouponBookResponseDto findCouponBook(CouponBookRequestDto couponBookRequestDto);
    Page<CouponBookResponseDto> findCouponBooks(Pageable pageable);
    Page<CouponTemplateResponseDto> findCouponBooksByBookId(Long bookId, Pageable pageable);
    Page<CouponBookDetailsForBookDto> findCouponBooksByCouponTemplateId(Long couponTemplateId, Pageable pageable);
}
