package shop.s5g.shop.service.coupon.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.entity.coupon.CouponTemplate.CouponTemplateType;
import shop.s5g.shop.exception.coupon.CouponAlreadyDeletedException;
import shop.s5g.shop.exception.coupon.CouponNotFoundException;
import shop.s5g.shop.repository.coupon.coupon.CouponRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.coupon.impl.CouponServiceImpl;

@ExtendWith(MockitoExtension.class)
class CouponServiceExceptionTest {

    @Mock
    private CouponTemplateRepository couponTemplateRepository;
    @Mock
    private CouponRepository couponRepository;
    @InjectMocks
    private CouponServiceImpl couponService;

    @Test
    @DisplayName("쿠폰 템플릿 null 값 체크 테스트")
    void createWelcomeCoupon_templateNullTest() {
        // Given
        String welcomeCouponName = CouponTemplateType.WELCOME.getTypeName();

        when(couponTemplateRepository.findParticularCouponByName(welcomeCouponName)).thenReturn(null);

        // When
        couponService.createWelcomeCoupon();

        // Then
        verify(couponTemplateRepository).findParticularCouponByName(welcomeCouponName);
        verifyNoInteractions(couponRepository);
    }

    @Test
    @DisplayName("쿠폰 ID가 null인 경우 IllegalArgumentException 발생")
    void getCoupon_NullId_ThrowsIllegalArgumentException() {
        // Given
        Long couponId = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> couponService.getCoupon(couponId)
        );

        assertEquals("쿠폰 아이디가 잘못 지정되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 ID가 음수인 경우 IllegalArgumentException 발생")
    void getCoupon_NegativeId_ThrowsIllegalArgumentException() {
        // Given
        Long couponId = -1L;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> couponService.getCoupon(couponId)
        );

        assertEquals("쿠폰 아이디가 잘못 지정되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않는 경우 CouponNotFoundException 발생")
    void getCoupon_CouponNotFound_ThrowsCouponNotFoundException() {
        // Given
        Long couponId = 1L;

        // Mock 설정: 쿠폰이 없을 경우
        when(couponRepository.findCoupon(couponId)).thenReturn(null);

        // When & Then
        CouponNotFoundException exception = assertThrows(
            CouponNotFoundException.class,
            () -> couponService.getCoupon(couponId)
        );

        assertEquals("선택하신 쿠폰을 찾으실 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 ID가 null인 경우 IllegalArgumentException 발생")
    void deleteCoupon_NullId_ThrowsIllegalArgumentException() {
        // Given
        Long couponId = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> couponService.deleteCoupon(couponId)
        );

        assertEquals("쿠폰 아이디의 값이 잘못 지정되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 ID가 0 이하인 경우 IllegalArgumentException 발생")
    void deleteCoupon_InvalidId_ThrowsIllegalArgumentException() {
        // Given
        Long couponId = -1L;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> couponService.deleteCoupon(couponId)
        );

        assertEquals("쿠폰 아이디의 값이 잘못 지정되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않는 경우 CouponNotFoundException 발생")
    void deleteCoupon_CouponNotFound_ThrowsCouponNotFoundException() {
        // Given
        Long couponId = 1L;

        // Mock 설정: 쿠폰 존재하지 않음
        when(couponRepository.existsById(couponId)).thenReturn(false);

        // When & Then
        CouponNotFoundException exception = assertThrows(
            CouponNotFoundException.class,
            () -> couponService.deleteCoupon(couponId)
        );

        assertEquals("해당 쿠폰은 존재하지 않는 쿠폰입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰이 이미 삭제된 경우 CouponAlreadyDeletedException 발생")
    void deleteCoupon_AlreadyDeleted_ThrowsCouponAlreadyDeletedException() {
        // Given
        Long couponId = 1L;

        // Mock 설정: 쿠폰은 존재하지만 삭제된 상태
        when(couponRepository.existsById(couponId)).thenReturn(true);
        when(couponRepository.checkActiveCoupon(couponId)).thenReturn(false);

        // When & Then
        CouponAlreadyDeletedException exception = assertThrows(
            CouponAlreadyDeletedException.class,
            () -> couponService.deleteCoupon(couponId)
        );

        assertEquals("해당 쿠폰은 삭제된 쿠폰입니다.", exception.getMessage());
    }
}
