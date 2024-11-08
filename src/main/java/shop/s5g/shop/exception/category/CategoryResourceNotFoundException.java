package shop.s5g.shop.exception.category;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class CategoryResourceNotFoundException extends ResourceNotFoundException {
    public CategoryResourceNotFoundException(String message) {
        super(message);
    }
}
