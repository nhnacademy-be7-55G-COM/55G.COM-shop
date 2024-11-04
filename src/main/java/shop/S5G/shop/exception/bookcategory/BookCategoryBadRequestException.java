package shop.S5G.shop.exception.bookcategory;

import shop.S5G.shop.exception.BadRequestException;

public class BookCategoryBadRequestException extends BadRequestException {
    public BookCategoryBadRequestException(String message) {
        super(message);
    }
}
