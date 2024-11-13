package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.BadRequestException;

public class PasswordIncorrectException extends BadRequestException {

    public PasswordIncorrectException(String message) {
        super(message);
    }
}
