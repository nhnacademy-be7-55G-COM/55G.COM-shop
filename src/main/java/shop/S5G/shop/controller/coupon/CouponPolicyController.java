package shop.S5G.shop.controller.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.service.coupon.CouponPolicyService;

@RestController
@RequiredArgsConstructor
public class CouponPolicyController {

    //TODO - Doo :  쿠폰 정책 컨트롤러 마무리 하기
    private final CouponPolicyService couponPolicyService;

}
