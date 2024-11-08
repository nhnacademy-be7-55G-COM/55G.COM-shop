package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.BadRequestException;

public class CouponTemplateBadRequestException extends BadRequestException {

    public CouponTemplateBadRequestException(String message) {
        super(message);
    }
}
