package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.BadRequestException;

public class CouponBadRequestException extends BadRequestException {

    public CouponBadRequestException(String message) {
        super(message);
    }
}
