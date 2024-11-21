package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.AlreadyExistsException;

public class CouponTemplateAlreadyExistsException extends AlreadyExistsException {

    public CouponTemplateAlreadyExistsException(String message) {
        super(message);
    }
}
