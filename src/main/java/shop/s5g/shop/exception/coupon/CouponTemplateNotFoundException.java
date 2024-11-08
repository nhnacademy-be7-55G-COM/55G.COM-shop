package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class CouponTemplateNotFoundException extends ResourceNotFoundException {

    public CouponTemplateNotFoundException(String message) {
        super(message);
    }
}
