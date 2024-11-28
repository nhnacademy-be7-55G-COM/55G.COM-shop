package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.AlreadyExistsException;

public class CouponAlreadyIssuedException extends AlreadyExistsException {

    public CouponAlreadyIssuedException(String message) {
        super(message);
    }
}
