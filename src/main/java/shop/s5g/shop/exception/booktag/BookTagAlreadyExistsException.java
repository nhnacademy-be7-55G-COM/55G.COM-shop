package shop.s5g.shop.exception.booktag;

import shop.s5g.shop.exception.AlreadyExistsException;

public class BookTagAlreadyExistsException extends AlreadyExistsException {
    public BookTagAlreadyExistsException(String message) {
        super(message);
    }
}
