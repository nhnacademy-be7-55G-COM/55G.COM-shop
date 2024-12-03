package shop.s5g.shop.service.coupon.book.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.exception.bookstatus.BookStatusResourceNotFoundException;
import shop.s5g.shop.exception.coupon.CouponBookAlreadyExistsException;
import shop.s5g.shop.exception.coupon.CouponBookNotFoundException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.coupon.book.CouponBookRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponBookServiceExceptionTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CouponBookRepository couponBookRepository;
    @Mock
    private CouponTemplateRepository couponTemplateRepository;
    @InjectMocks
    private CouponBookServiceImpl couponBookService;

    private CouponBookRequestDto couponBookRequestDto;

    @BeforeEach
    public void setUp() {
        couponBookRequestDto = new CouponBookRequestDto(1L, 1L);
    }

    @Test
    @DisplayName("존재하지 않는 책 검사")
    void couponBookNotFoundException() {
        // Given
        when(couponBookRepository.existsByBookAndCouponTemplate(50L, 50L)).thenReturn(false);

        // When & Then
        CouponBookNotFoundException exception = assertThrows(CouponBookNotFoundException.class, () ->{
            couponBookService.getCouponBook(new CouponBookRequestDto(50L, 50L));
        });

        assertEquals("해당 couponBook은 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("판매하지 않는 책 검사")
    void bookNotForSaleException() {
        // Given
        Book book = new Book(new Publisher(),
            new BookStatus(), "테스트", "테스트 챕터", "설명", LocalDate.of(2024,11,11),
            "1111", 3000L, new BigDecimal("0.5"), true, 10, 3000L, LocalDateTime.now(), LocalDateTime.now());

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.findBookStatus(anyLong())).thenReturn("OFFSALE");

        // When & Then
        BookStatusResourceNotFoundException exception = assertThrows(
            BookStatusResourceNotFoundException.class, () -> {
            couponBookService.createCouponBook(couponBookRequestDto);
        });

        assertEquals("해당 책은 판매중이 아닙니다.", exception.getMessage());
    }

    @Test
    @DisplayName("활성화되지 않은 템플릿 검사")
    void NotActiveCouponTemplateExceptionTest() {

        // Given
        Book book = new Book(new Publisher(),
            new BookStatus(), "테스트", "테스트 챕터", "설명", LocalDate.of(2024,11,11),
            "1111", 3000L, new BigDecimal("0.5"), true, 10, 3000L, LocalDateTime.now(), LocalDateTime.now());

        CouponTemplate deleteCouponTemplate = new CouponTemplate(
            new CouponPolicy(
                new BigDecimal(1000),
                20000L,
                null,
                30
            ),
            "Test Coupon",
            "This is Test Coupon"
        );
        deleteCouponTemplate.setActive(false);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.findBookStatus(anyLong())).thenReturn("ONSALE");
        when(couponTemplateRepository.findById(anyLong())).thenReturn(Optional.of(deleteCouponTemplate));
        when(couponTemplateRepository.checkActiveCouponTemplate(any())).thenReturn(false);

        CouponTemplateNotFoundException exception = assertThrows(
            CouponTemplateNotFoundException.class, () -> {
                couponBookService.createCouponBook(couponBookRequestDto);
            }
        );

        assertEquals("해당 쿠폰 템플릿은 삭제된 템플릿입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("해당 책에 대한 쿠폰이 존재하는 지 검사")
    void existsBookExceptionTest() {

        // Given
        Book book = new Book(new Publisher(),
            new BookStatus(), "테스트", "테스트 챕터", "설명", LocalDate.of(2024,11,11),
            "1111", 3000L, new BigDecimal("0.5"), true, 10, 3000L, LocalDateTime.now(), LocalDateTime.now());

        CouponTemplate deleteCouponTemplate = new CouponTemplate(
            new CouponPolicy(
                new BigDecimal(1000),
                20000L,
                null,
                30
            ),
            "Test Coupon",
            "This is Test Coupon"
        );

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.findBookStatus(anyLong())).thenReturn("ONSALE");
        when(couponTemplateRepository.findById(anyLong())).thenReturn(Optional.of(deleteCouponTemplate));
        when(couponTemplateRepository.checkActiveCouponTemplate(any())).thenReturn(true);
        when(couponBookRepository.existsByBookAndCouponTemplate(any(), any())).thenReturn(true);

        CouponBookAlreadyExistsException exception = assertThrows(
            CouponBookAlreadyExistsException.class, () -> {
                couponBookService.createCouponBook(couponBookRequestDto);
            }
        );

        assertEquals("해당 책에 대한 쿠폰이 이미 존재합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("책 아이디 오류 검사 - 음수 값")
    void bookId_IllegalArgumentExceptionTest() {
        // Given
        Long wrongBookId = -1L;
        Pageable pageable = PageRequest.of(0, 10);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            couponBookService.getCouponBooksByBookId(wrongBookId, pageable);
        });

        assertEquals("잘못된 책 아이디 값 요청입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("책 아이디 오류 검사 - null 값")
    void bookId_NullIllegalArgumentExceptionTest() {
        // Given
        Long nullBookId = null;
        Pageable pageable = PageRequest.of(0, 10);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            couponBookService.getCouponBooksByBookId(nullBookId, pageable);
        });

        assertEquals("잘못된 책 아이디 값 요청입니다.", exception.getMessage());
    }
}
