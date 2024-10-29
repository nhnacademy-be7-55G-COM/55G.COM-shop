package shop.S5G.shop.exception.order;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class WrappingPaperDoesNotExistsException extends ResourceNotFoundException {

    public WrappingPaperDoesNotExistsException(long id) {
        super("Wrapping paper does not exist with given id: "+id);
    }
}
