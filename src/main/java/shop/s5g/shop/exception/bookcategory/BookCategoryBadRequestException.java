package shop.s5g.shop.exception.bookcategory;

import shop.s5g.shop.exception.BadRequestException;

public class BookCategoryBadRequestException extends BadRequestException {
    public BookCategoryBadRequestException(String message) {
        super(message);
    }
}
