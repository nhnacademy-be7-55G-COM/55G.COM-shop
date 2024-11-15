package shop.s5g.shop.exception.order;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class WrappingPaperDoesNotExistsException extends ResourceNotFoundException {

    public WrappingPaperDoesNotExistsException(long id) {
        super("Wrapping paper does not exist with given id: "+id);
    }
}
