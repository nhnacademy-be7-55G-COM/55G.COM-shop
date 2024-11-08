package shop.s5g.shop.exception.bookstatus;

import shop.s5g.shop.exception.BadRequestException;

public class BookStatusBadRequestException extends BadRequestException {
    public BookStatusBadRequestException(String message) {
        super(message);
    }
}
