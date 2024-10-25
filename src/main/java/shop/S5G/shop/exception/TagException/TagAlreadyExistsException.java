package shop.S5G.shop.exception.TagException;

import shop.S5G.shop.exception.AlreadyExistsException;

public class TagAlreadyExistsException extends AlreadyExistsException {
    public TagAlreadyExistsException(String message) {
        super(message);
    }
}
