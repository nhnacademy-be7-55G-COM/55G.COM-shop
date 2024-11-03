package shop.S5G.shop.exception.bookstatus;

import shop.S5G.shop.exception.AlreadyExistsException;

public class BookStatusAlreadyExistsException extends AlreadyExistsException {
    public BookStatusAlreadyExistsException(String message) {
        super(message);
    }
}
