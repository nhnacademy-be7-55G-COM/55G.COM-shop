package shop.s5g.shop.exception.order;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class OrderDetailsNotExistException extends ResourceNotFoundException {

    public OrderDetailsNotExistException(String message) {
        super(message);
    }
}
