package shop.s5g.shop.exception.coupon;

import lombok.Getter;
import shop.s5g.shop.exception.ErrorCode;

@Getter
public class CouponPolicyValidationException extends RuntimeException {

    private final ErrorCode errorCode;

    public CouponPolicyValidationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
