package shop.s5g.shop.service.coupon.template;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateUpdateRequestDto;
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
    void getCouponTemplate() {
        // Given
        Long couponTemplateId = 1L;

        CouponTemplateResponseDto couponTemplateResponseDto = new CouponTemplateResponseDto(
            couponTemplateId,
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
        CouponTemplateResponseDto templateDto = couponTemplateService.getCouponTemplate(couponTemplateId);

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

        CouponTemplateUpdateRequestDto couponTemplateRequestDto = new CouponTemplateUpdateRequestDto(
            couponTemplate.getCouponTemplateId(),
            couponTemplate.getCouponName(),
            couponTemplate.getCouponDescription()
        );

        when(couponTemplateRepository.existsById(couponTemplateId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)).thenReturn(true);


        // When
        couponTemplateService.updateCouponTemplate(couponTemplateId, couponTemplateRequestDto);

        // Then
        verify(couponTemplateRepository, times(1)).updateCouponTemplate(couponTemplateId, couponTemplateRequestDto);
        verify(couponTemplateRepository, times(1)).existsById(couponTemplateId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(couponTemplateId);
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

    @Test
    @DisplayName("쿠폰 템플릿 조회 - Pageable")
    void getCouponTemplates() {
        // Given
        couponTemplateRepository.save(couponTemplate);

        Pageable pageable = PageRequest.of(0, 10);

        List<CouponTemplateResponseDto> couponTemplateResponseDto = List.of(
            new CouponTemplateResponseDto(
                1L,
                new BigDecimal("0.1"),
                20000L,
                50000L,
                30,
                "생일 쿠폰",
                "이 쿠폰은 생일자를 위한 쿠폰입니다.")
        );

        Page<CouponTemplateResponseDto> pageDto = new PageImpl<>(couponTemplateResponseDto, pageable, couponTemplateResponseDto.size());

        when(couponTemplateRepository.findCouponTemplatesByPageable(pageable)).thenReturn(pageDto);

        // When
        Page<CouponTemplateResponseDto> result = couponTemplateService.getCouponTemplates(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(couponTemplateResponseDto);

        CouponTemplateResponseDto templateDto = result.getContent().get(0);
        assertThat(templateDto).isNotNull();
        assertThat(templateDto.maxPrice()).isEqualTo(50000L);
        assertThat(templateDto.couponName()).isEqualTo("생일 쿠폰");
    }

    @Test
    @DisplayName("사용되지 않은 쿠폰 템플릿 조회")
    void findUnusedCouponTemplates() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponTemplateResponseDto> couponTemplateResponseDto = List.of(
            new CouponTemplateResponseDto(
                1L,
                new BigDecimal("0.1"),
                20000L,
                50000L,
                30,
                "생일 쿠폰",
                "이 쿠폰은 생일자를 위한 쿠폰입니다.")
        );

        Page<CouponTemplateResponseDto> pageDto = new PageImpl<>(couponTemplateResponseDto, pageable, couponTemplateResponseDto.size());

        when(couponTemplateRepository.findUnusedCouponTemplates(any(Pageable.class)))
            .thenReturn(pageDto);

        // When
        Page<CouponTemplateResponseDto> result = couponTemplateService.getCouponTemplatesUnused(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(couponTemplateResponseDto);

        CouponTemplateResponseDto templateDto = result.getContent().get(0);
        assertThat(templateDto).isNotNull();
        assertThat(templateDto.maxPrice()).isEqualTo(50000L);
        assertThat(templateDto.couponName()).isEqualTo("생일 쿠폰");
        assertThat(templateDto.couponDescription()).isEqualTo("이 쿠폰은 생일자를 위한 쿠폰입니다.");
    }

    @Test
    @DisplayName("생일쿠폰과 웰컴쿠폰을 제외한 쿠폰 발급")
    void getCouponTemplateExcludingWelcomeAndBirth() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponTemplateResponseDto> couponTemplateResponseDto = List.of(
            new CouponTemplateResponseDto(
                2L,
                new BigDecimal("0.2"),
                30000L,
                5000L,
                30,
                "Book Coupon",
                "이 쿠폰은 책에 쓰이는 쿠폰입니다."
            )
        );

        Page<CouponTemplateResponseDto> pageDto = new PageImpl<>(couponTemplateResponseDto, pageable, couponTemplateResponseDto.size());

        when(couponTemplateRepository.findCouponTemplatesExcludingBirthAndWelcome(pageable)).thenReturn(pageDto);

        Page<CouponTemplateResponseDto> result = couponTemplateService.getCouponTemplateExcludingWelcomeAndBirth(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }
}
