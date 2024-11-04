package shop.S5G.shop.exception.bookstatus;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class BookStatusResourceNotFoundException extends ResourceNotFoundException {
    public BookStatusResourceNotFoundException(String message) {
        super(message);
    }
}
