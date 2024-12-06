package shop.s5g.shop.service.coupon.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
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
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.CouponTemplate.CouponTemplateType;
import shop.s5g.shop.repository.coupon.coupon.CouponRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.coupon.impl.CouponServiceImpl;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponTemplateRepository couponTemplateRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private CouponTemplate couponTemplate;

    @BeforeEach
    void setUp() {
        couponTemplate = new CouponTemplate(
            new CouponPolicy(
                new BigDecimal("0.5"),
                20000L,
                null,
                30
            ),
            "Welcome Coupon",
            "This is a Welcome Coupon"
        );
    }

    @Test
    @DisplayName("회원가입 쿠폰 생성 테스트")
    void createWelcomeCouponTest() {
        // Given
        String welcomeCoupon = CouponTemplateType.WELCOME.getTypeName();

        when(couponTemplateRepository.findParticularCouponByName(welcomeCoupon)).thenReturn(couponTemplate);

        // When
        couponService.createWelcomeCoupon();

        // Then
        verify(couponTemplateRepository, times(1)).findParticularCouponByName(welcomeCoupon);
    }

    @Test
    @DisplayName("쿠폰 코드로 쿠폰 조회 테스트")
    void getCouponCodeTest() {
        // Given
        String couponCode = "1q2w3e4r5t";

        Coupon coupon = new Coupon(
            couponTemplate,
            couponCode
        );

        when(couponRepository.findByCouponCode(couponCode)).thenReturn(coupon);

        // When
        Coupon result = couponService.getCouponByCode(couponCode);

        // Then
        verify(couponRepository, times(1)).findByCouponCode(couponCode);
        assertEquals(coupon.getCouponCode(), result.getCouponCode());
    }

    @Test
    @DisplayName("특정 쿠폰 조회 테스트")
    void getCouponTest() {
        // Given
        Long couponId = 1L;
        CouponResponseDto couponResponseDto = new CouponResponseDto(
            couponId,
            1L,
            "1q2w3e4r5t"
        );

        when(couponRepository.findCoupon(couponId)).thenReturn(couponResponseDto);

        // When
        CouponResponseDto result = couponService.getCoupon(couponId);

        // Then
        verify(couponRepository, times(1)).findCoupon(couponId);

        assertEquals(couponResponseDto.couponCode(), result.couponCode());
    }

    @Test
    @DisplayName("발급한 모든 쿠폰 조회 테스트")
    void getAllCouponsTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponResponseDto> couponList = List.of(
            new CouponResponseDto(1L, 1L, "1q2w3e4r5t"),
            new CouponResponseDto(2L, 1L, "1q2w3e4r5q")
        );
        Page<CouponResponseDto> page = new PageImpl<>(couponList, pageable, couponList.size());

        when(couponRepository.getAllIssuedCoupons(pageable)).thenReturn(page);

        // When
        Page<CouponResponseDto> result = couponService.getAllCouponList(pageable);

        // Then
        verify(couponRepository, times(1)).getAllIssuedCoupons(pageable);

        assertEquals(page.getTotalElements(), result.getTotalElements());
        assertEquals(page.getTotalPages(), result.getTotalPages());
        assertEquals(page.getNumber(), result.getNumber());
    }

    @Test
    @DisplayName("쿠폰 삭제 테스트")
    void deleteCouponTest() {
        // Given
        Long couponId = 1L;

        doNothing().when(couponRepository).deleteCouponById(couponId);
        when(couponRepository.existsById(couponId)).thenReturn(true);
        when(couponRepository.checkActiveCoupon(couponId)).thenReturn(true);

        // When & Then
        couponService.deleteCoupon(couponId);

        verify(couponRepository, times(1)).deleteCouponById(couponId);
    }

    @Test
    @DisplayName("발급 가능한 쿠폰 조회 테스트")
    void getAvailableCouponTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<AvailableCouponResponseDto> couponResponseList = List.of(
            new AvailableCouponResponseDto(1L, 1L, "Happy Coupon", 20000L, null, new BigDecimal("0.5"), 5L),
            new AvailableCouponResponseDto(2L, 1L, "Happy Coupon", 20000L, null, new BigDecimal("0.5"), 30L)
        );
        Page<AvailableCouponResponseDto> coupons = new PageImpl<>(couponResponseList, pageable, couponResponseList.size());

        when(couponRepository.getAllAvailableCoupons(pageable)).thenReturn(coupons);

        // When
        Page<AvailableCouponResponseDto> result = couponService.getAvailableCoupons(pageable);

        // Then
        verify(couponRepository, times(1)).getAllAvailableCoupons(pageable);
    }
}
