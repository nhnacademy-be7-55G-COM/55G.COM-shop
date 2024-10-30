package shop.S5G.shop.exception.coupon;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class CouponTemplateNotFoundException extends ResourceNotFoundException {

    public CouponTemplateNotFoundException(String message) {
        super(message);
    }
}
