package shop.S5G.shop.exception.order;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class OrderDetailsNotExistException extends ResourceNotFoundException {

    public OrderDetailsNotExistException(String message) {
        super(message);
    }
}
