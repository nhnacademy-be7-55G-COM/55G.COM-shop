package shop.s5g.shop.repository.coupon.couponBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.config.TestQueryFactoryConfig;
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

@DataJpaTest
@ActiveProfiles("local")
@Import(TestQueryFactoryConfig.class)
class CouponBookRepositoryTest {
    @Autowired
    private CouponTemplateRepository couponTemplateRepository;
    @Autowired
    private CouponPolicyRepository couponPolicyRepository;
    @Autowired
    private CouponBookRepository couponBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookStatusRepository bookStatusRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    private CouponBookRequestDto couponBookRequestDto;
    private Book book;
    private CouponTemplate bookCouponTemplate;

    @BeforeEach
    void setUp() {
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy);

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        bookCouponTemplate = new CouponTemplate(
            couponPolicy,
            "도서쿠폰",
            "테스트용 도서 쿠폰입니다."
        );
        couponTemplateRepository.save(couponTemplate);
        couponTemplateRepository.save(bookCouponTemplate);

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

        CouponBook couponBook = new CouponBook(
            bookCouponTemplate,
            book
        );

        couponBookRepository.save(couponBook);

        couponBookRequestDto = new CouponBookRequestDto(
            bookCouponTemplate.getCouponTemplateId(),
            book.getBookId()
        );
    }

    @Test
    @DisplayName("책 쿠폰이 존재하는지 체크")
    void existCouponBookTest() {

        boolean dslExists = couponBookRepository.existsByBookAndCouponTemplate(book.getBookId(),
            couponBookRequestDto.couponTemplateId());

        assertTrue(dslExists);
    }
    
    @Test
    @DisplayName("특정 책 쿠폰 확인")
    void findCouponBook() {

        CouponBookResponseDto couponBook = couponBookRepository.findCouponBook(couponBookRequestDto);

        assertNotNull(couponBook);
        assertEquals("도서쿠폰", couponBook.couponName());
        assertEquals("아낌없이 주는 나무", couponBook.title());
        assertEquals("테스트용 도서 쿠폰입니다.", couponBook.couponDescription());
    }

    @Test
    @DisplayName("책 쿠폰 조회")
    void findCouponBooks() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponBookResponseDto> bookList = couponBookRepository.findCouponBooks(pageable);

        assertNotNull(bookList);
        assertEquals(1, bookList.getTotalElements());
        assertEquals(1, bookList.getTotalPages());
        assertEquals(1, bookList.getContent().size());
    }

    @Test
    @DisplayName("책에 적용된 쿠폰 템플릿 조회")
    void findCouponTemplatesByCouponBook() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponTemplateResponseDto> templateList = couponBookRepository.findCouponBooksByBookId(book.getBookId(), pageable);

        assertNotNull(templateList);
        assertEquals(1, templateList.getTotalElements());
        assertEquals(1, templateList.getTotalPages());
        assertEquals(1, templateList.getContent().size());

        CouponTemplateResponseDto result = templateList.getContent().get(0);

        assertEquals("도서쿠폰", result.couponName());
        assertEquals("테스트용 도서 쿠폰입니다.", result.couponDescription());
        assertEquals(20000L, result.condition());
    }

    @Test
    @DisplayName("쿠폰이 적용된 책 조회")
    void findBooksByCouponBook() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponBookDetailsForBookDto> bookList = couponBookRepository.findCouponBooksInfo(pageable);

        assertNotNull(bookList);
        assertEquals(1, bookList.getContent().size());
        assertEquals(1, bookList.getTotalElements());
        assertEquals(1, bookList.getTotalPages());

        CouponBookDetailsForBookDto result = bookList.getContent().get(0);

        assertEquals("아낌없이 주는 나무", result.title());
    }
}
