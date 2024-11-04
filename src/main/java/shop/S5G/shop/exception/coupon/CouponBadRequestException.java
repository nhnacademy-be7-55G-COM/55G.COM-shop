package shop.S5G.shop.exception.coupon;

import shop.S5G.shop.exception.BadRequestException;

public class CouponBadRequestException extends BadRequestException {

    public CouponBadRequestException(String message) {
        super(message);
    }
}
