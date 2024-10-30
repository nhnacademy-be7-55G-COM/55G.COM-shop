package shop.S5G.shop.exception.coupon;

import shop.S5G.shop.exception.BadRequestException;

public class CouponTemplateBadRequestException extends BadRequestException {

    public CouponTemplateBadRequestException(String message) {
        super(message);
    }
}
