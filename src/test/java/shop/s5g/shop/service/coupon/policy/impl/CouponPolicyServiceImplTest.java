package shop.s5g.shop.service.coupon.policy.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
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
import shop.s5g.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceImplTest {

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    private CouponPolicy testCouponPolicy;

    @BeforeEach
    void setUp() {
        testCouponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.1"))
            .maxPrice(50000L)
            .condition(2000L)
            .duration(30)
            .build();
    }

    @Test
    @DisplayName("쿠폰 정책 생성 - 서비스")
    void saveCouponPolicy() {
        // Given
        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("0.1"),
            50000L,
            2000L,
            30
        );

        when(couponPolicyRepository.save(any(CouponPolicy.class))).thenReturn(testCouponPolicy);

        // When
        CouponPolicy couponPolicy = couponPolicyService.saveCouponPolicy(couponPolicyRequestDto);

        // Then
        assertThat(couponPolicy).isNotNull();
        assertThat(couponPolicy.getCondition()).isEqualTo(testCouponPolicy.getCondition());
        assertThat(couponPolicy.getDuration()).isEqualTo(testCouponPolicy.getDuration());
        assertThat(couponPolicy.getMaxPrice()).isEqualTo(testCouponPolicy.getMaxPrice());
        assertThat(couponPolicy.getDiscountPrice()).isEqualTo(testCouponPolicy.getDiscountPrice());

        verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
    }

    @Test
    @DisplayName("쿠폰 정책 업데이트 - 서비스")
    void updateCouponPolicy() {
        // Given
        Long couponPolicyId = 1L;

        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
                                                            new BigDecimal("0.5"),
                                                            25000L,
                                                            2000L,
                                                            30
                                                        );

        when(couponPolicyRepository.existsById(couponPolicyId)).thenReturn(true);

        // When
        couponPolicyService.updateCouponPolicy(couponPolicyId, couponPolicyRequestDto);

        // Then
        verify(couponPolicyRepository, times(1))
            .updateCouponPolicy(
                couponPolicyId,
            couponPolicyRequestDto.discountPrice(),
            couponPolicyRequestDto.condition(),
            couponPolicyRequestDto.maxPrice(),
            couponPolicyRequestDto.duration()
            );
    }

    @Test
    @DisplayName("특정 쿠폰 정책 조회 - 서비스")
    void findCouponPolicyById() {
        // Given
        Long couponPolicyId = 1L;

        when(couponPolicyRepository.findById(couponPolicyId)).thenReturn(Optional.of(testCouponPolicy));

        // When
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.getByCouponPolicyId(couponPolicyId);

        // Then
        assertThat(couponPolicyResponseDto).isNotNull();
        assertThat(couponPolicyResponseDto.discountPrice()).isEqualTo(testCouponPolicy.getDiscountPrice());
        assertThat(couponPolicyResponseDto.duration()).isEqualTo(testCouponPolicy.getDuration());
        assertThat(couponPolicyResponseDto.condition()).isEqualTo(testCouponPolicy.getCondition());
        assertThat(couponPolicyResponseDto.maxPrice()).isEqualTo(testCouponPolicy.getMaxPrice());

        verify(couponPolicyRepository, times(1)).findById(couponPolicyId);
    }

    @Test
    @DisplayName("쿠폰 정책 조회")
    void getCouponPolicies() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponPolicyResponseDto> policies = Arrays.asList(
            new CouponPolicyResponseDto(1L, new BigDecimal("0.5"), 25000L, 3000L, 15),
            new CouponPolicyResponseDto(2L, new BigDecimal("5000"), 20000L, 2500L, 10)
        );

        Page<CouponPolicyResponseDto> pagedPolicies = new PageImpl<>(policies, pageable, policies.size());

        when(couponPolicyRepository.findAllCouponPolicies(pageable)).thenReturn(pagedPolicies);

        // When
        Page<CouponPolicyResponseDto> result = couponPolicyService.getAllCouponPolices(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);

        CouponPolicyResponseDto policy1 = result.getContent().get(0);
        assertThat(policy1.discountPrice()).isEqualTo(new BigDecimal("0.5"));
        assertThat(policy1.maxPrice()).isEqualTo(3000L);
        assertThat(policy1.duration()).isEqualTo(15);

        verify(couponPolicyRepository, times(1)).findAllCouponPolicies(pageable);
    }
}
