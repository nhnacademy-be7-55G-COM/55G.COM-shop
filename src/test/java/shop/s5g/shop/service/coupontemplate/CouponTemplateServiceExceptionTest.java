package shop.s5g.shop.service.coupontemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.template.impl.CouponTemplateServiceImpl;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponTemplateServiceExceptionTest {

    @Mock
    private CouponTemplateRepository couponTemplateRepository;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @InjectMocks
    private CouponTemplateServiceImpl couponTemplateService;

    @Test
    @DisplayName("쿠폰 템플릿 생성 - 쿠폰 정책 ID가 존재하지 않을 경우")
    void createTemplateThrowsCouponPolicyNotFoundException() {
        // Given
        Long notFoundId = 999L;

        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            notFoundId,
            "Error Coupon",
            "이 쿠폰은 에러 테스트를 위한 쿠폰입니다."
        );
        when(couponPolicyRepository.findById(notFoundId)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> couponTemplateService.createCouponTemplate(couponTemplateRequestDto))
            .isInstanceOf(CouponPolicyNotFoundException.class)
            .hasMessage("선택하신 쿠폰 정책이 존재하지 않습니다.");

        // Then
        verify(couponPolicyRepository, times(1)).findById(notFoundId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 조회 - 잘못된 ID로 IllegalArgumentException 발생")
    void findTemplateThrowsIllegalArgumentException() {
        // Given
        Long nullId = null;

        Long invalidId = -1L;

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.findCouponTemplate(invalidId))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> couponTemplateService.findCouponTemplate(nullId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 템플릿 조회 - 쿠폰 조회 템플릿이 없을 때")
    void findTemplateThrowsCouponTemplateNotFoundException() {
        // Given
        Long notFoundId = 999L;

        when(couponTemplateRepository.existsById(notFoundId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.findCouponTemplate(notFoundId))
            .isInstanceOf(CouponTemplateNotFoundException.class)
            .hasMessage("쿠폰 템플릿이 존재하지 않습니다.");

        // Then
        verify(couponTemplateRepository, times(1)).existsById(notFoundId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 조회 - 삭제된 쿠폰 템플릿 ID로 CouponPolicyNotFoundException 발생")
    void findCouponTemplateThrowsCouponPolicyNotFoundException() {
        // Given
        Long deletedId = 1L;

        when(couponTemplateRepository.existsById(deletedId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(deletedId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.findCouponTemplate(deletedId))
            .isInstanceOf(CouponPolicyNotFoundException.class)
            .hasMessage("삭제된 쿠폰 템플릿입니다.");

        // Then
        verify(couponTemplateRepository, times(1)).existsById(deletedId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(deletedId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 - 잘못된 ID로 IllegalArgumentException 발생")
    void updateCouponTemplateThrowsIllegalArgumentException() {
        // Given
        Long nullId = null;

        Long invalidId = -1L;

        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            1L,
            "Error Coupon",
            "이 쿠폰은 에러 쿠폰입니다."
        );

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.updateCouponTemplate(nullId, couponTemplateRequestDto))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> couponTemplateService.updateCouponTemplate(invalidId, couponTemplateRequestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 - 존재하지 않는 쿠폰 템플릿 ID로 CouponTemplateNotFoundException 발생")
    void updateCouponTemplateThrowsCouponTemplateNotFoundException() {
        // Given
        Long notFoundId = 999L;
        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            1L,
            "Error Coupon",
            "이 쿠폰은 에러 쿠폰입니다."
        );

        when(couponTemplateRepository.existsById(notFoundId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.updateCouponTemplate(notFoundId, couponTemplateRequestDto))
            .isInstanceOf(CouponTemplateNotFoundException.class)
            .hasMessage("쿠폰 템플릿이 존재하지 않습니다.");

        // Then
        verify(couponTemplateRepository, times(1)).existsById(notFoundId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 - 삭제된 쿠폰 템플릿 ID로 CouponTemplateNotFoundException 발생")
    void updateCouponTemplateThrowsDeletedCouponTemplateException() {
        // Given
        Long deletedId = 1L;
        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            1L,
            "Error Coupon",
            "이 쿠폰은 에러 쿠폰입니다."
        );

        when(couponTemplateRepository.existsById(deletedId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(deletedId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.updateCouponTemplate(deletedId, couponTemplateRequestDto))
            .isInstanceOf(CouponTemplateNotFoundException.class)
            .hasMessage("삭제된 쿠폰 템플릿입니다.");

        // Then
        verify(couponTemplateRepository, times(1)).existsById(deletedId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(deletedId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 - 존재하지 않는 쿠폰 정책으로 CouponPolicyNotFoundException 발생")
    void updateCouponTemplateThrowsCouponPolicyNotFoundException() {
        // Given
        Long couponTemplateId = 1L;
        Long couponPolicyId = 999L;
        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            couponPolicyId,
            "Coupon Name",
            "Coupon Description"
        );

        when(couponTemplateRepository.existsById(couponTemplateId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)).thenReturn(true);
        when(couponPolicyRepository.findById(couponPolicyId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.updateCouponTemplate(couponTemplateId, couponTemplateRequestDto))
            .isInstanceOf(CouponPolicyNotFoundException.class)
            .hasMessage("선택하신 쿠폰 정책이 존재하지 않습니다.");

        // Then
        verify(couponTemplateRepository, times(1)).existsById(couponTemplateId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(couponTemplateId);
        verify(couponPolicyRepository, times(1)).findById(couponPolicyId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 삭제 - 잘못된 ID로 IllegalArgumentException 발생")
    void deleteCouponTemplateThrowsIllegalArgumentException() {
        // Given
        Long nullId = null;

        Long invalidId = -1L;
        // When & Then
        assertThatThrownBy(() -> couponTemplateService.deleteCouponTemplate(nullId))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> couponTemplateService.deleteCouponTemplate(invalidId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 템플릿 삭제 - 존재하지 않는 쿠폰 템플릿 ID로 CouponTemplateNotFoundException 발생")
    void deleteCouponTemplateThrowsCouponTemplateNotFoundException() {
        // Given
        Long notFoundId = 999L;

        when(couponTemplateRepository.existsById(notFoundId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.deleteCouponTemplate(notFoundId))
            .isInstanceOf(CouponTemplateNotFoundException.class)
            .hasMessage("쿠폰 템플릿이 존재하지 않습니다.");

        // Then
        verify(couponTemplateRepository, times(1)).existsById(notFoundId);
    }

    @Test
    @DisplayName("쿠폰 템플릿 삭제 - 이미 삭제된 쿠폰 템플릿 ID로 CouponTemplateNotFoundException 발생")
    void deleteCouponTemplateThrowsAlreadyDeletedCouponTemplateException() {
        // Given
        Long deletedId = 1L;

        when(couponTemplateRepository.existsById(deletedId)).thenReturn(true);
        when(couponTemplateRepository.checkActiveCouponTemplate(deletedId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> couponTemplateService.deleteCouponTemplate(deletedId))
            .isInstanceOf(CouponTemplateNotFoundException.class)
            .hasMessage("이미 삭제된 쿠폰 템플릿입니다.");

        // Then
        verify(couponTemplateRepository, times(1)).existsById(deletedId);
        verify(couponTemplateRepository, times(1)).checkActiveCouponTemplate(deletedId);
    }
    
}
