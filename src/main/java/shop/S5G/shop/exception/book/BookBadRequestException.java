package shop.S5G.shop.exception.book;

import shop.S5G.shop.exception.BadRequestException;

public class BookBadRequestException extends BadRequestException {
    public BookBadRequestException(String message) {
        super(message);
    }
}
