package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.AlreadyDeletedRecordException;

public class CouponAlreadyDeletedException extends AlreadyDeletedRecordException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
