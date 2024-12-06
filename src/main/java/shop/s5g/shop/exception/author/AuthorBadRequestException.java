package shop.s5g.shop.exception.author;

import shop.s5g.shop.exception.BadRequestException;

public class AuthorBadRequestException extends BadRequestException {
    public AuthorBadRequestException(String message) {
        super(message);
    }
}
