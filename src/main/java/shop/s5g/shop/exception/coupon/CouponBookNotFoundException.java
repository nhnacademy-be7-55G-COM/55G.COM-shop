package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class CouponBookNotFoundException extends ResourceNotFoundException {

    public CouponBookNotFoundException(String message) {
        super(message);
    }
}
