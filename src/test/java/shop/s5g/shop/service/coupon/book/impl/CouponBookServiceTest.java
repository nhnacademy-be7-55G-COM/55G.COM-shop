package shop.s5g.shop.service.coupon.book.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.dto.coupon.book.CouponBookDetailsForBookDto;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.coupon.CouponBook;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.repository.coupon.book.CouponBookRepository;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponBookServiceTest {

    @Mock
    private CouponBookRepository couponBookRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CouponPolicyRepository couponPolicyRepository;
    @Mock
    private CouponTemplateRepository couponTemplateRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private CouponBookServiceImpl couponBookService;

    private CouponPolicy couponPolicy;
    private CouponTemplate couponTemplate;
    private Book book;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy);

        couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        couponTemplateRepository.save(couponTemplate);

        Publisher publisher = new Publisher();
        BookStatus bookStatus = new BookStatus();

        bookStatusRepository.save(bookStatus);
        publisherRepository.save(publisher);

        book = new Book(
            publisher,
            bookStatus,
            "아낌없이 주는 나무",
            "전래동화",
            "이 책은 전래동화 입니다.",
            LocalDate.of(2000, 10, 10),
            "978-3-15-148410-2",
            15000L,
            new BigDecimal("5.5"),
            true,
            200,
            2000L,
            LocalDateTime.of(2010, 5, 5, 15, 30),
            LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        bookRepository.save(book);
    }

    @Test
    @DisplayName("쿠폰 책 생성")
    void createCouponBook() {
        // Given
        CouponBookRequestDto couponBookRequest = new CouponBookRequestDto(
            couponTemplate.getCouponTemplateId(),
            book.getBookId()
        );

        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        when(bookRepository.findBookStatus(book.getBookId())).thenReturn("ONSALE");
        when(couponTemplateRepository.findById(couponTemplate.getCouponTemplateId()))
            .thenReturn(Optional.of(couponTemplate));
        when(couponTemplateRepository.checkActiveCouponTemplate(couponTemplate.getCouponTemplateId()))
            .thenReturn(true);
        when(couponBookRepository.existsByBookAndCouponTemplate(book.getBookId(), couponTemplate.getCouponTemplateId()))
            .thenReturn(false);

        // When
        couponBookService.createCouponBook(couponBookRequest);

        // Then
        verify(couponBookRepository, times(1)).save(any(CouponBook.class));
        verify(bookRepository, times(1)).findById(book.getBookId());
    }

    @Test
    @DisplayName("특정 쿠폰 책 조회")
    void findCouponBook() {
        // Given
        CouponBookRequestDto couponBookRequest = new CouponBookRequestDto(
            book.getBookId(),
            couponTemplate.getCouponTemplateId()
        );

        CouponBookResponseDto expectedResponse = new CouponBookResponseDto(
            book.getTitle(),
            couponPolicy.getDiscountPrice(),
            couponPolicy.getCondition(),
            couponPolicy.getMaxPrice(),
            couponPolicy.getDuration(),
            couponTemplate.getCouponName(),
            couponTemplate.getCouponDescription()
        );

        when(couponBookRepository.existsByBookAndCouponTemplate(
            couponBookRequest.bookId(),
            couponBookRequest.couponTemplateId())
        ).thenReturn(true);
        when(couponBookRepository.findCouponBook(couponBookRequest)).thenReturn(expectedResponse);

        // When
        CouponBookResponseDto result = couponBookService.getCouponBook(couponBookRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo(book.getTitle());
        assertThat(result.couponName()).isEqualTo(couponTemplate.getCouponName());
        assertThat(result.couponDescription()).isEqualTo(couponTemplate.getCouponDescription());
    }

    @Test
    @DisplayName("쿠폰 책 리스트 조회")
    void getCouponBooks() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponBookResponseDto> couponBookList = List.of(
            new CouponBookResponseDto(
                "아낌없이 주는 나무",
                new BigDecimal("0.5"),
                20000L,
                5000L,
                30,
                "생일 쿠폰",
                "생일자를 위한 할인 쿠폰입니다."
            )
        );

        Page<CouponBookResponseDto> expectedPage = new PageImpl<>(couponBookList, pageable, couponBookList.size());

        when(couponBookRepository.findCouponBooks(pageable)).thenReturn(expectedPage);

        // When
        Page<CouponBookResponseDto> result = couponBookService.getCouponBooks(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(couponBookList);

        CouponBookResponseDto couponBookDto = result.getContent().get(0);
        assertThat(couponBookDto.title()).isEqualTo("아낌없이 주는 나무");
        assertThat(couponBookDto.discountPrice()).isEqualTo(new BigDecimal("0.5"));
        assertThat(couponBookDto.couponName()).isEqualTo("생일 쿠폰");
        assertThat(couponBookDto.couponDescription()).isEqualTo("생일자를 위한 할인 쿠폰입니다.");
    }

    @Test
    @DisplayName("책 ID로 쿠폰 템플릿 조회")
    void givenValidBookId_whenGetCouponBooksByBookId_thenReturnsCouponBooks() {
        // given
        Long validBookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        CouponTemplateResponseDto couponTemplateResponseDto = new CouponTemplateResponseDto(
            1L, new BigDecimal("0.5"), 20000L, 10000L, 70, "할인쿠폰", "책 구매시 사용 가능"
        );
        List<CouponTemplateResponseDto> responseList = Collections.singletonList(couponTemplateResponseDto);
        Page<CouponTemplateResponseDto> couponBooksPage = new PageImpl<>(responseList, pageable, responseList.size());

        given(couponBookRepository.findCouponBooksByBookId(validBookId, pageable)).willReturn(couponBooksPage);

        // when
        Page<CouponTemplateResponseDto> result = couponBookService.getCouponBooksByBookId(validBookId, pageable);

        // then
        assertThat(result).isNotNull();
        verify(couponBookRepository).findCouponBooksByBookId(validBookId, pageable);
    }

    @Test
    @DisplayName("템플릿 ID로 책 조회")
    void givenValidTemplateId_whenGetCouponBooksByTemplateId_thenReturnsCouponBookDetails() {
        // given
        Long validTemplateId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        CouponBookDetailsForBookDto detailsDto = new CouponBookDetailsForBookDto(1L, "베스트셀러 책 제목");
        List<CouponBookDetailsForBookDto> detailsList = Collections.singletonList(detailsDto);
        Page<CouponBookDetailsForBookDto> detailsPage = new PageImpl<>(detailsList, pageable, detailsList.size());

        given(couponBookRepository.findCouponBooksInfo(pageable)).willReturn(detailsPage);

        // when
        Page<CouponBookDetailsForBookDto> result = couponBookService.getCouponBooksByTemplateId(pageable);

        // then
        assertThat(result).isNotNull();
        verify(couponBookRepository).findCouponBooksInfo(pageable);
    }
}
