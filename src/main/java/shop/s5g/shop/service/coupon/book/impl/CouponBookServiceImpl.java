package shop.s5g.shop.service.coupon.book.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.book.CouponBookDetailsForBookDto;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.coupon.CouponBook;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.bookstatus.BookStatusResourceNotFoundException;
import shop.s5g.shop.exception.coupon.CouponBookAlreadyExistsException;
import shop.s5g.shop.exception.coupon.CouponBookNotFoundException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.coupon.book.CouponBookRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.book.CouponBookService;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponBookServiceImpl implements CouponBookService {

    private final CouponBookRepository couponBookRepository;
    private final BookRepository bookRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    /**
     * 특정 책에 대한 쿠폰 생성
     * @param couponBookRequestDto
     */
    @Override
    public void createCouponBook(CouponBookRequestDto couponBookRequestDto) {

        Book book = bookRepository.findById(couponBookRequestDto.bookId())
            .orElseThrow(() -> new BookResourceNotFoundException("해당 책을 찾을 수 없습니다."));

        if (!bookRepository.findBookStatus(couponBookRequestDto.bookId()).equalsIgnoreCase("ONSALE")) {
            throw new BookStatusResourceNotFoundException("해당 책은 판매중이 아닙니다.");
        }

        CouponTemplate couponTemplate = couponTemplateRepository.findById(couponBookRequestDto.couponTemplateId())
            .orElseThrow(() -> new CouponTemplateNotFoundException("해당 쿠폰 템플릿을 찾을 수 없습니다."));

        if (!couponTemplateRepository.checkActiveCouponTemplate(couponTemplate.getCouponTemplateId())) {
            throw new CouponTemplateNotFoundException("해당 쿠폰 템플릿은 삭제된 템플릿입니다.");
        }

        if (couponBookRepository.existsByBookAndCouponTemplate(book.getBookId(), couponTemplate.getCouponTemplateId())) {
            throw new CouponBookAlreadyExistsException("해당 책에 대한 쿠폰이 이미 존재합니다.");
        }

        couponBookRepository.save(
            new CouponBook(
                couponTemplate,
                book
            )
        );
    }

    /**
     * 특정 쿠폰 책 조회
     * @param couponBookRequestDto
     * @return CouponBookResponseDto
     */
    @Override
    public CouponBookResponseDto getCouponBook(CouponBookRequestDto couponBookRequestDto) {

        if (!couponBookRepository.existsByBookAndCouponTemplate(
            couponBookRequestDto.bookId(),
            couponBookRequestDto.couponTemplateId())
        ) {
            throw new CouponBookNotFoundException("해당 couponBook은 존재하지 않습니다.");
        }

        return couponBookRepository.findCouponBook(couponBookRequestDto);
    }

    /**
     * 쿠폰 책 조회 - Pageable
     * @param pageable
     * @return Page<CouponBookResponseDto>
     */
    @Override
    public Page<CouponBookResponseDto> getCouponBooks(Pageable pageable) {
        return couponBookRepository.findCouponBooks(pageable);
    }

    /**
     * 특정 책에 대해 적용된 쿠폰 조회 - pageable
     * @param bookId
     * @param pageable
     * @return Page<CouponTemplateResponseDto>
     */
    @Override
    public Page<CouponTemplateResponseDto> getCouponBooksByBookId(Long bookId, Pageable pageable) {

        if (Objects.isNull(bookId) || bookId <= 0) {
            throw new IllegalArgumentException("잘못된 책 아이디 값 요청입니다.");
        }

        return couponBookRepository.findCouponBooksByBookId(bookId, pageable);
    }

    /**
     * 특정 쿠폰 템플릿이 적용된 책 조회
     * @param pageable
     * @return Page<CouponBookDetailsForBookDto>
     */
    @Override
    public Page<CouponBookDetailsForBookDto> getCouponBooksByTemplateId(Pageable pageable) {

        return couponBookRepository.findCouponBooksInfo(pageable);
    }
}
