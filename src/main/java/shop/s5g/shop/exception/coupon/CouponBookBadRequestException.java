package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.BadRequestException;

public class CouponBookBadRequestException extends BadRequestException {

    public CouponBookBadRequestException(String message) {
        super(message);
    }
}
