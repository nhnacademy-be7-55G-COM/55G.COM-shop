package shop.s5g.shop.repository.coupon.coupontemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
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
import shop.s5g.shop.config.QueryFactoryConfig;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.coupon.CouponBook;
import shop.s5g.shop.entity.coupon.CouponCategory;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.bookstatus.BookStatusRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.coupon.book.CouponBookRepository;
import shop.s5g.shop.repository.coupon.category.CouponCategoryRepository;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;

@DataJpaTest
@ActiveProfiles("local")
@Import(QueryFactoryConfig.class)
class CouponTemplateRepositoryTest {

    @Autowired
    private CouponTemplateRepository couponTemplateRepository;
    @Autowired
    private CouponPolicyRepository couponPolicyRepository;
    @Autowired
    private CouponCategoryRepository couponCategoryRepository;
    @Autowired
    private CouponBookRepository couponBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookStatusRepository bookStatusRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EntityManager entityManager;

    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy);
    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 - create")
    void save() {

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        CouponTemplate saveCouponTemplate = couponTemplateRepository.save(couponTemplate);

        assertEquals(couponPolicy, saveCouponTemplate.getCouponPolicy());
        assertEquals("생일쿠폰", saveCouponTemplate.getCouponName());
        assertEquals("이 달의 생일자를 위한 할인 쿠폰입니다.", saveCouponTemplate.getCouponDescription());
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 - update")
    void update() {

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        CouponTemplate saveCouponTemplate = couponTemplateRepository.save(couponTemplate);
        saveCouponTemplate.setCouponDescription("이번 달 생일자들을 위한 생일 쿠폰입니다.");

        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            saveCouponTemplate.getCouponPolicy().getCouponPolicyId(),
            saveCouponTemplate.getCouponName(),
            saveCouponTemplate.getCouponDescription()
        );

        couponTemplateRepository.updateCouponTemplate(
            saveCouponTemplate.getCouponTemplateId(),
            saveCouponTemplate.getCouponPolicy(),
            couponTemplateRequestDto);

        CouponTemplateResponseDto couponTemplateResponseDto = couponTemplateRepository.findCouponTemplateById(couponTemplate.getCouponTemplateId());

        assertEquals("이번 달 생일자들을 위한 생일 쿠폰입니다.", couponTemplateResponseDto.couponDescription());
    }

    @Test
    @DisplayName("쿠폰 템플릿 조회 - read")
    void findCouponTemplate() {

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        CouponTemplate saveCouponTemplate = couponTemplateRepository.save(couponTemplate);

        CouponTemplateResponseDto couponTemplateResponseDto = couponTemplateRepository.findCouponTemplateById(saveCouponTemplate.getCouponTemplateId());

        assertEquals("생일쿠폰", couponTemplateResponseDto.couponName());
        assertEquals("이 달의 생일자를 위한 할인 쿠폰입니다.", couponTemplateResponseDto.couponDescription());
    }

    @Test
    @DisplayName("쿠폰 템플릿 삭제 - delete")
    void deleteCouponTemplate() {
        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );
        couponTemplateRepository.save(couponTemplate);

        couponTemplateRepository.deleteCouponTemplate(couponTemplate.getCouponTemplateId());

        entityManager.flush();
        entityManager.clear();

        CouponTemplate findTemplate = couponTemplateRepository.findById(couponTemplate.getCouponTemplateId()).get();

        assertFalse(findTemplate.isActive());
    }

    @Test
    @DisplayName("쿠폰 템플릿 활성 상태 확인 - 존재하는 활성 쿠폰 템플릿")
    void checkActiveCouponTemplateReturnsTrue() {
        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );
        couponTemplateRepository.save(couponTemplate);

        boolean isActive = couponTemplateRepository.checkActiveCouponTemplate(
            couponTemplate.getCouponTemplateId());

        assertTrue(isActive);
    }

    @Test
    @DisplayName("쿠폰 템플릿 활성 상태 확인 - 존재하지 않는 비활성화 쿠폰 템플릿")
    void checkActiveCouponTemplateReturnsFalse() {
        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );
        couponTemplateRepository.save(couponTemplate);

        couponTemplateRepository.deleteCouponTemplate(couponTemplate.getCouponTemplateId());

        boolean isActive = couponTemplateRepository.checkActiveCouponTemplate(
            couponTemplate.getCouponTemplateId());

        assertFalse(isActive);
    }

    @Test
    @DisplayName("쿠폰 템플릿 활성화된 리스트 조회")
    void findAllCouponTemplatesOnActive() {
        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );
        couponTemplateRepository.save(couponTemplate);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponTemplateResponseDto> result = couponTemplateRepository.findCouponTemplatesByPageable(pageable);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("도서와 카테고리에 사용되지 않은 쿠폰 템플릿 조회 테스트")
    void findCouponTemplateUnused() {
        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        CouponTemplate bookCouponTemplate = new CouponTemplate(
            couponPolicy,
            "도서쿠폰",
            "테스트용 도서 쿠폰입니다."
        );

        CouponTemplate categoryCouponTemplate = new CouponTemplate(
            couponPolicy,
            "카테고리쿠폰",
            "테스트용 카테고리 쿠폰입니다."
        );

        couponTemplateRepository.save(couponTemplate);
        couponTemplateRepository.save(bookCouponTemplate);
        couponTemplateRepository.save(categoryCouponTemplate);

        Publisher publisher = new Publisher();
        BookStatus bookStatus = new BookStatus();
        bookStatusRepository.save(bookStatus);
        publisherRepository.save(publisher);

        Book book = new Book(
            publisher,
            bookStatus,
            "아낌없이 주는 나무",
            "전래동화",
            "이 책은 전래동화 입니다.",
            LocalDateTime.of(2000, 10, 10, 10, 50),
            "978-3-15-148410-2",
            15000L,
            new BigDecimal("5.5"),
            true,
            200,
            2000L,
            LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        bookRepository.save(book);

        CouponBook couponBook = new CouponBook(
            bookCouponTemplate,
            book
        );

        Category category = new Category(
            null,
            "테스트",
            true
        );
        categoryRepository.save(category);

        CouponCategory couponCategory = new CouponCategory(
            categoryCouponTemplate,
            category
        );

        couponBookRepository.save(couponBook);
        couponCategoryRepository.save(couponCategory);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponTemplateResponseDto> result = couponTemplateRepository.findUnusedCouponTemplates(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(1, result.getContent().size());

        CouponTemplateResponseDto responseDto = result.getContent().get(0);
        Assertions.assertEquals(responseDto.couponName(), couponTemplate.getCouponName());
        Assertions.assertEquals(responseDto.couponDescription(), couponTemplate.getCouponDescription());
    }
}
