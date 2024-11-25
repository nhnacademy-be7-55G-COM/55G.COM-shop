package shop.s5g.shop.exception.author;

import shop.s5g.shop.exception.AlreadyExistsException;

public class AuthorAlreadyExistsException extends AlreadyExistsException {
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}
