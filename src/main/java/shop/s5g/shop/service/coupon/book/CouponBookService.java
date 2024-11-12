package shop.s5g.shop.service.coupon.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.book.CouponBookDetailsForBookDto;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;

public interface CouponBookService {

    // Create
    void createCouponBook(CouponBookRequestDto couponBookRequestDto);

    // read
    CouponBookResponseDto getCouponBook(CouponBookRequestDto couponBookRequestDto);

    Page<CouponBookResponseDto> getCouponBooks(Pageable pageable);

    Page<CouponTemplateResponseDto> getCouponBooksByBookId(Long bookId, Pageable pageable);

    Page<CouponBookDetailsForBookDto> getCouponBooksByTemplateId(Long templateId, Pageable pageable);

    // update 가 필요한 것인가??

    // delete 이 부분은 쿠폰 템플릿이 삭제되면 그 때 삭제되게 구현
}
