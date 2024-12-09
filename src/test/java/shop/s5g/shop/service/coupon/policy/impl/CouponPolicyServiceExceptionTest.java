package shop.s5g.shop.service.coupon.policy.impl;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import shop.s5g.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.s5g.shop.exception.ErrorCode;
import shop.s5g.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.s5g.shop.exception.coupon.CouponPolicyValidationException;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceExceptionTest {

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
        assertThatThrownBy(() -> couponPolicyService.getByCouponPolicyId(invalidId))
            .isInstanceOf(IllegalArgumentException.class);

        // When & Then - couponPolicyId가 null 인 경우
        assertThatThrownBy(() -> couponPolicyService.getByCouponPolicyId(null))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("쿠폰 정책 조회 예외 처리 테스트 - 쿠폰 정책 Id가 존재하지 않을 경우")
    void findCouponPolicyByIdThrowCouponPolicyNotFoundException() {
        // Given
        Long notFoundId = 999L;

        // When & Then
        assertThatThrownBy(() -> couponPolicyService.getByCouponPolicyId(notFoundId))
            .isInstanceOf(CouponPolicyNotFoundException.class)
            .hasMessageContaining(notFoundId + ", 아이디는 존재하지 않는 쿠폰 정책입니다");

    }

    @Test
    @DisplayName("80% 이상 예외 처리 테스트")
    void testDiscountExceeds80Percent() {

        // Given
        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("0.85"),
            10000L,
            null,
            30
        );

        when(couponPolicyRepository.existsById(any())).thenReturn(true);

        // When & Then
        CouponPolicyValidationException exception = assertThrows(
            CouponPolicyValidationException.class,
            () -> couponPolicyService.updateCouponPolicy(1L, couponPolicyRequestDto)
        );

        assertEquals(ErrorCode.DISCOUNT_EXCEEDS_80_PERCENT, exception.getErrorCode());
    }

    @Test
    @DisplayName("유효하지 않은 범위 테스트")
    void testDiscountInvalidRangeLow() {
        // Given
        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("500"),
            10000L,
            null,
            30
        );

        // When & Then
        when(couponPolicyRepository.existsById(any())).thenReturn(true);

        CouponPolicyValidationException exception = assertThrows(
            CouponPolicyValidationException.class,
            () -> couponPolicyService.updateCouponPolicy(1L, couponPolicyRequestDto)
        );

        assertEquals(ErrorCode.DISCOUNT_INVALID_RANGE, exception.getErrorCode());
    }

    @Test
    @DisplayName("할인금액 유효하지 않은 범위 테스트")
    void testDiscountInvalidRangeHigh() {

        // Given
        CouponPolicyRequestDto requestDto = new CouponPolicyRequestDto(
            new BigDecimal("9000"),
            10000L,
            null,
            30
        );

        when(couponPolicyRepository.existsById(any())).thenReturn(true);

        // When & Then
        CouponPolicyValidationException exception = assertThrows(
            CouponPolicyValidationException.class,
            () -> couponPolicyService.updateCouponPolicy(1L, requestDto)
        );

        assertEquals(ErrorCode.DISCOUNT_INVALID_RANGE, exception.getErrorCode());
    }

    @Test
    @DisplayName("최대 금액 예외 처리 테스트")
    void testMaxPriceExceedsLimit() {
        // Given
        CouponPolicyRequestDto requestDto = new CouponPolicyRequestDto(
            new BigDecimal("1000"),
            10000L,
            6000L,
            30
        );

        when(couponPolicyRepository.existsById(any())).thenReturn(true);

        // When & Then
        CouponPolicyValidationException exception = assertThrows(
            CouponPolicyValidationException.class,
            () -> couponPolicyService.updateCouponPolicy(1L, requestDto)
        );

        assertEquals(ErrorCode.MAX_PRICE_EXCEEDS_LIMIT, exception.getErrorCode());
    }

    @Test
    @DisplayName("maxPrice가 null일 때 예외가 발생하지 않음")
    void validateCouponPolicy_maxPriceIsNull_NoExceptionThrown() {
        // Given
        CouponPolicyRequestDto requestDto = new CouponPolicyRequestDto(
            new BigDecimal("1000"),
            10000L,
            null,
            30
        );
        when(couponPolicyRepository.existsById(any())).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> couponPolicyService.updateCouponPolicy(1L, requestDto));
    }
}
