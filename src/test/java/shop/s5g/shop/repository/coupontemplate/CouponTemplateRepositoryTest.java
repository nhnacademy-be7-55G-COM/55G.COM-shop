package shop.s5g.shop.repository.coupontemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;

@DataJpaTest
@ActiveProfiles("local")
class CouponTemplateRepositoryTest {

    @Autowired
    private CouponTemplateRepository couponTemplateRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

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

        assertEquals(couponPolicy, couponTemplateResponseDto.couponPolicy());
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
}
