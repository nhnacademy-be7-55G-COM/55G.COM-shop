package shop.s5g.shop.exception.book;

import shop.s5g.shop.exception.BadRequestException;

public class BookBadRequestException extends BadRequestException {
    public BookBadRequestException(String message) {
        super(message);
    }
}
