package shop.s5g.shop.exception.bookcategory;

import shop.s5g.shop.exception.AlreadyExistsException;

public class BookCategoryAlreadyExistsException extends AlreadyExistsException {
    public BookCategoryAlreadyExistsException(String message) {
        super(message);
    }
}
