package shop.s5g.shop.exception.coupon;

import shop.s5g.shop.exception.ErrorCode;

public class CouponPolicyValidationException extends RuntimeException {

    private final ErrorCode errorCode;

    public CouponPolicyValidationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
