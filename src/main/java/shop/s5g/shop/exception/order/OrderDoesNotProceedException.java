package shop.s5g.shop.exception.order;

import shop.s5g.shop.exception.AlreadyDeletedRecordException;

public class OrderDoesNotProceedException extends AlreadyDeletedRecordException {

    public OrderDoesNotProceedException(String message) {
        super(message);
    }
}
