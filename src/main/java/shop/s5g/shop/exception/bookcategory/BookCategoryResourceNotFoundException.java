package shop.s5g.shop.exception.bookcategory;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class BookCategoryResourceNotFoundException extends ResourceNotFoundException {
    public BookCategoryResourceNotFoundException(String message) {
        super(message);
    }
}
