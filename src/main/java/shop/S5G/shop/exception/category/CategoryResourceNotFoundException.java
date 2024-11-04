package shop.S5G.shop.exception.category;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class CategoryResourceNotFoundException extends ResourceNotFoundException {
    public CategoryResourceNotFoundException(String message) {
        super(message);
    }
}
