package shop.S5G.shop.exception.book;

import shop.S5G.shop.exception.AlreadyExistsException;

public class BookAlreadyExistsException extends AlreadyExistsException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
