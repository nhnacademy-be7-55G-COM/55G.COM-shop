package shop.S5G.shop.couponpolicy.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyRequestDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;
import shop.S5G.shop.repository.coupon.CouponPolicyRepository;
import shop.S5G.shop.service.coupon.CouponPolicyService;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CouponPolicyServiceTest {

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @InjectMocks
    private CouponPolicyService couponPolicyService;

    @Test
    @DisplayName("쿠폰 정책 생성")
    void createCouponPolicy() {
        // Given : 어떠한 데이터가 주어질 때.
        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("0.1"),
            20000L,
            2000L,
            30
        );
//
//        when(couponPolicyRepository.existsById(1L)).thenReturn(false);
//        when(couponPolicyRepository.save(any(CouponPolicy.class))).thenReturn(new CouponPolicy());


        // When : 어떠한 기능을 실행하면.


        // Then : 어떠한 결과를 기대한다.


    }
}
