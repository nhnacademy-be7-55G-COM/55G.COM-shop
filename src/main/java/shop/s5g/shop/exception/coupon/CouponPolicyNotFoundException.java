package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class CouponPolicyNotFoundException extends ResourceNotFoundException {

    public CouponPolicyNotFoundException(String message) {
        super(message);
    }
}
