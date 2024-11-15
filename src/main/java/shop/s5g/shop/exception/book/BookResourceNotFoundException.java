package shop.s5g.shop.exception.book;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class BookResourceNotFoundException extends ResourceNotFoundException {
    public BookResourceNotFoundException(String message) {
        super(message);
    }
}
