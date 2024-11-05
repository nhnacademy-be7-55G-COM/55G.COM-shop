package shop.S5G.shop.exception.coupon;

import shop.S5G.shop.exception.AlreadyDeletedRecordException;

public class CouponAlreadyDeletedException extends AlreadyDeletedRecordException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
