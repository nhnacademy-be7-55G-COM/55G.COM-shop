package shop.s5g.shop.exception.book;

import shop.s5g.shop.exception.AlreadyExistsException;

public class BookAlreadyExistsException extends AlreadyExistsException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
