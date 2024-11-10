package shop.s5g.shop.exception.tag;

import shop.s5g.shop.exception.AlreadyExistsException;

public class TagAlreadyExistsException extends AlreadyExistsException {
    public TagAlreadyExistsException(String message) {
        super(message);
    }
}
