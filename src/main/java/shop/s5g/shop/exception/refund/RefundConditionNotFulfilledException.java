package shop.s5g.shop.exception.refund;

import shop.s5g.shop.exception.BadRequestException;

public class RefundConditionNotFulfilledException extends BadRequestException {
    public RefundConditionNotFulfilledException(String message) {
        super(message);
    }
}
