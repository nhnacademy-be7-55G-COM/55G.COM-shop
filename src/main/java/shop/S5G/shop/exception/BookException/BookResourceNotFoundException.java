package shop.S5G.shop.exception.BookException;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class BookResourceNotFoundException extends ResourceNotFoundException {
    public BookResourceNotFoundException(String message) {
        super(message);
    }
}
