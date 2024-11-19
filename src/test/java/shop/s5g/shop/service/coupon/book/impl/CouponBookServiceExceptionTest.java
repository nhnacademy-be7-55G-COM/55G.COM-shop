package shop.s5g.shop.service.coupon.book.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
    private CouponTemplateRepository couponTemplateRepository;
    @Mock
    private CouponBookRepository couponBookRepository;
    @InjectMocks
    private CouponBookServiceImpl couponBookService;

    private CouponBookRequestDto couponBookRequestDto;

    @BeforeEach
    public void setUp() {
        couponBookRequestDto = new CouponBookRequestDto(1L, 1L); // 예시로 bookId와 couponTemplateId를 설정
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
            new BookStatus(), "테스트", "테스트 챕터", "설명", LocalDateTime.now(),
            "1111", 3000L, new BigDecimal("0.5"), true, 10, 3000L, LocalDateTime.now());

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
    @DisplayName("유효하지 않은 templateId가 전달될 때 IllegalArgumentException 발생")
    void givenInvalidTemplateId_whenGetCouponBooksByTemplateId_thenThrowsIllegalArgumentException() {
        // given
        Long invalidTemplateId = -1L;
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> couponBookService.getCouponBooksByTemplateId(invalidTemplateId, pageable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("잘못된 템플릿 아이디 값 요청입니다.");
    }

    @Test
    @DisplayName("유효하지 않은 bookId가 전달될 때 IllegalArgumentException 발생")
    void givenInvalidBookId_whenGetCouponBooksByBookId_thenThrowsIllegalArgumentException() {
        // given
        Long invalidBookId = -1L;
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> couponBookService.getCouponBooksByBookId(invalidBookId, pageable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("잘못된 책 아이디 값 요청입니다.");
    }
}
