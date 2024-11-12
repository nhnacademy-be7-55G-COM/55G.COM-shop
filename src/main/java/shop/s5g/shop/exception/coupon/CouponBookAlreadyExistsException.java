package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.AlreadyExistsException;

public class CouponBookAlreadyExistsException extends AlreadyExistsException {

    public CouponBookAlreadyExistsException(String message) {
        super(message);
    }
}
