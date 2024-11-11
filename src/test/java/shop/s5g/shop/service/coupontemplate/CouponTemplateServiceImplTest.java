package shop.s5g.shop.service.coupontemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.template.impl.CouponTemplateServiceImpl;

/**
 * 쿠폰 템플릿 서비스 테스트
 */
@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponTemplateServiceImplTest {

    @Mock
    private CouponTemplateRepository couponTemplateRepository;
    @Mock
    private CouponPolicyRepository couponPolicyRepository;
    @InjectMocks
    private CouponTemplateServiceImpl couponTemplateService;

    private CouponTemplate couponTemplate;
    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.1"))
            .maxPrice(50000L)
            .condition(2000L)
            .duration(30)
            .build();

        couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일 쿠폰",
            "이 쿠폰은 생일자를 위한 쿠폰입니다."
        );

    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 - create")
    void save() {
        // Given
        CouponTemplateRequestDto testTemplate = new CouponTemplateRequestDto(
            couponPolicy.getCouponPolicyId(),
            "생일 쿠폰",
            "이 쿠폰은 생일자를 위한 쿠폰입니다."
        );
        when(couponPolicyRepository.findById(couponPolicy.getCouponPolicyId())).thenReturn(Optional.of(couponPolicy));
        when(couponTemplateRepository.save(any(CouponTemplate.class))).thenReturn(couponTemplate);

        // When
        couponTemplateService.createCouponTemplate(testTemplate);

        // Then
        verify(couponTemplateRepository, times(1)).save(any(CouponTemplate.class));

        verify(couponPolicyRepository, times(1)).findById(couponPolicy.getCouponPolicyId());
    }

    @Test
    @DisplayName("쿠폰 템플릿 조회 - read")
    void findCouponTemplate() {
        // Given
        Long couponTemplateId = 1L;

        CouponTemplateResponseDto couponTemplateResponseDto = new CouponTemplateResponseDto(
            couponPolicy.getDiscountPrice(),
            couponPolicy.getCondition(),
            couponPolicy.getMaxPrice(),
            couponPolicy.getDuration(),
            "생일 쿠폰",
            "이 쿠폰은 생일자를 위한 쿠폰입니다."
        );

        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            1L,
            "생일 쿠폰",
            "이 쿠폰은 생일자를 위한 쿠폰입니다."
        );

        when(couponTemplateRepository.existsById(couponTemplateId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)).thenReturn(true);
        when(couponTemplateRepository.findCouponTemplateById(couponTemplateId)).thenReturn(couponTemplateResponseDto);

        // When
        CouponTemplateResponseDto templateDto = couponTemplateService.findCouponTemplate(couponTemplateId);

        // Then
        assertThat(templateDto).isNotNull();
        assertThat(templateDto.couponName()).isEqualTo(couponTemplateResponseDto.couponName());
        assertThat(templateDto.couponDescription()).isEqualTo(couponTemplateResponseDto.couponDescription());

        verify(couponTemplateRepository, times(1)).existsById(couponTemplateId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(couponTemplateId);
        verify(couponTemplateRepository, times(1)).findCouponTemplateById(couponTemplateId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 - update")
    void updateCouponTemplate() {
        // Given
        Long couponTemplateId = 1L;

        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            couponPolicy.getCouponPolicyId(),
            couponTemplate.getCouponName(),
            couponTemplate.getCouponDescription()
        );

        when(couponTemplateRepository.existsById(couponTemplateId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)).thenReturn(true);

        when(couponPolicyRepository.findById(couponPolicy.getCouponPolicyId())).thenReturn(Optional.of(couponPolicy));

        // When
        couponTemplateService.updateCouponTemplate(couponTemplateId, couponTemplateRequestDto);

        // Then
        verify(couponTemplateRepository, times(1)).updateCouponTemplate(couponTemplateId, couponPolicy, couponTemplateRequestDto);
        verify(couponTemplateRepository, times(1)).existsById(couponTemplateId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(couponTemplateId);

        verify(couponPolicyRepository, times(1)).findById(couponPolicy.getCouponPolicyId());
    }

    @Test
    @DisplayName("쿠폰 템플릿 삭제 - delete")
    void deleteCouponTemplate() {
        // Given
        Long templateId = 1L;

        when(couponTemplateRepository.existsById(templateId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(templateId)).thenReturn(true);

        // When
        couponTemplateService.deleteCouponTemplate(templateId);

        // Then
        verify(couponTemplateRepository, times(1)).deleteCouponTemplate(templateId);
        verify(couponTemplateRepository, times(1)).existsById(templateId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(templateId);
    }
}
