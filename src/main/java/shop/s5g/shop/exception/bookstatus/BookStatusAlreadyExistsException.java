package shop.s5g.shop.exception.bookstatus;

import shop.s5g.shop.exception.AlreadyExistsException;

public class BookStatusAlreadyExistsException extends AlreadyExistsException {
    public BookStatusAlreadyExistsException(String message) {
        super(message);
    }
}
