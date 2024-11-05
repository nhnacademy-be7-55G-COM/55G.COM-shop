package shop.S5G.shop.exception.bookcategory;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class BookCategoryResourceNotFoundException extends ResourceNotFoundException {
    public BookCategoryResourceNotFoundException(String message) {
        super(message);
    }
}
