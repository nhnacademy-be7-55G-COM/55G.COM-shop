package shop.S5G.shop.exception.coupon;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class CouponPolicyNotFoundException extends ResourceNotFoundException {

    public CouponPolicyNotFoundException(String message) {
        super(message);
    }
}
