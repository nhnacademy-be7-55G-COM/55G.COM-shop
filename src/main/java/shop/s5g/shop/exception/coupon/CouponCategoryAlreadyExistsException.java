package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.AlreadyExistsException;

public class CouponCategoryAlreadyExistsException extends AlreadyExistsException {

    public CouponCategoryAlreadyExistsException(String message) {
        super(message);
    }
}
