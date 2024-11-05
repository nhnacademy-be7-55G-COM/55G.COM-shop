package shop.S5G.shop.exception.bookstatus;

import shop.S5G.shop.exception.BadRequestException;

public class BookStatusBadRequestException extends BadRequestException {
    public BookStatusBadRequestException(String message) {
        super(message);
    }
}
