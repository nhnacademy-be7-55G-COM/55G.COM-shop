package shop.S5G.shop.exception.bookcategory;

import shop.S5G.shop.exception.AlreadyExistsException;

public class BookCategoryAlreadyExistsException extends AlreadyExistsException {
    public BookCategoryAlreadyExistsException(String message) {
        super(message);
    }
}
