package shop.S5G.shop.service.coponpolicy.impl;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyRequestDto;
import shop.S5G.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.S5G.shop.repository.coupon.CouponPolicyRepository;
import shop.S5G.shop.service.coupon.impl.CouponPolicyServiceImpl;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
public class CouponPolicyServiceExceptionTest {

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    @Test
    @DisplayName("쿠폰 정책 수정 예외 처리 테스트 - 잘못된 값 넣었을 경우")
    void updateCouponExceptionThrowIllegalArgumentException() {
        // Given
        Long invalidId = -1L;

        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("0.1"),
            50000L,
            2000L,
            30
        );

        // When & Then- couponPolicyId가 null 인 경우
        assertThatThrownBy(() -> couponPolicyService.updateCouponPolicy(null, couponPolicyRequestDto))
            .isInstanceOf(IllegalArgumentException.class);

        // When & Then - couponPolicyId가 0 이하인 경우
        assertThatThrownBy(() -> couponPolicyService.updateCouponPolicy(invalidId, couponPolicyRequestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 정책 예외 처리 테스트 - 쿠폰 정책 Id가 존재하지 않을 경우")
    void updateCouponExceptionThrowCouponPolicyNotFoundException() {
        // Given
        Long notFoundId = 999L;

        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("0.1"),
            50000L,
            2000L,
            30
        );

        when(couponPolicyRepository.existsById(notFoundId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> couponPolicyService.updateCouponPolicy(notFoundId, couponPolicyRequestDto))
            .isInstanceOf(CouponPolicyNotFoundException.class)
            .hasMessageContaining(notFoundId + ", 아이디는 존재하지 않는 쿠폰 정책입니다");

        verify(couponPolicyRepository, times(1)).existsById(notFoundId);
    }

    @Test
    @DisplayName("쿠폰 정책 조회 예외 처리 테스트 - 잘못된 값을 집어넣을 경우")
    void findCouponPolicyByIdThrowIllegalArgumentException() {
        // Given
        Long invalidId = -1L;

        // When & Then - couponPolicyId가 0 이하인 경우
        assertThatThrownBy(() -> couponPolicyService.findByCouponPolicyId(invalidId))
            .isInstanceOf(IllegalArgumentException.class);

        // When & Then - couponPolicyId가 null 인 경우
        assertThatThrownBy(() -> couponPolicyService.findByCouponPolicyId(null))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("쿠폰 정책 조회 예외 처리 테스트 - 쿠폰 정책 Id가 존재하지 않을 경우")
    void findCouponPolicyByIdThrowCouponPolicyNotFoundException() {
        // Given
        Long notFoundId = 999L;

        // When & Then
        assertThatThrownBy(() -> couponPolicyService.findByCouponPolicyId(notFoundId))
            .isInstanceOf(CouponPolicyNotFoundException.class)
            .hasMessageContaining(notFoundId + ", 아이디는 존재하지 않는 쿠폰 정책입니다");

    }
}
