package shop.s5g.shop.exception.bookstatus;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class BookStatusResourceNotFoundException extends ResourceNotFoundException {
    public BookStatusResourceNotFoundException(String message) {
        super(message);
    }
}
