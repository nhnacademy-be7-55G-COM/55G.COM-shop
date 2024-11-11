package shop.s5g.shop.controller.coupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.coupon.book.CouponBookDetailsForBookDto;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.coupon.CouponBookBadRequestException;
import shop.s5g.shop.service.coupon.book.CouponBookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/coupons")
public class CouponBookController {

    private final CouponBookService couponBookService;

    /**
     * 책 쿠폰 생성 - API
     * @param couponBookRequestDto
     * @param result
     * @return MessageDto
     */
    @PostMapping("/book")
    public ResponseEntity<MessageDto> createCouponBook(
        @Valid @RequestBody CouponBookRequestDto couponBookRequestDto,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new CouponBookBadRequestException("잘못된 요청입니다.");
        }

        couponBookService.createCouponBook(couponBookRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("책 쿠폰 생성 성공"));
    }

    /**
     * 책에 적용된 쿠폰 모두 조회 - API
     * @param pageable
     * @return Page<CouponBookResponseDto>
     */
    @GetMapping("/books")
    public ResponseEntity<Page<CouponBookResponseDto>> findAllCouponBooks(Pageable pageable) {
        Page<CouponBookResponseDto> couponBookList = couponBookService.findCouponBooks(pageable);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponBookList);
    }

    /**
     * 특정 책에 적용된 쿠폰 템플릿 조회 - API
     * @param bookId
     * @param pageable
     * @return Page<CouponTemplateResponseDto>
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<CouponTemplateResponseDto>> getCouponBooksByBookId(@PathVariable("bookId") Long bookId,
        Pageable pageable) {

        Page<CouponTemplateResponseDto> templateList = couponBookService.findCouponBooksByBookId(bookId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(templateList);
    }

    /**
     * 특정 템플릿이 적용된 책 조회 - API
     * @param templateId
     * @param pageable
     * @return Page<CouponBookDetailsForBookDto>
     */
    @GetMapping("/book/{templateId}")
    public ResponseEntity<Page<CouponBookDetailsForBookDto>> getCouponBooksByTemplateId(@PathVariable("templateId") Long templateId,
        Pageable pageable) {

        Page<CouponBookDetailsForBookDto> bookList = couponBookService.findCouponBooksByTemplateId(templateId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }
}
