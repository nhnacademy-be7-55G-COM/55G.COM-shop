package shop.S5G.shop.exception.CategoryException;

import shop.S5G.shop.exception.AlreadyExistsException;

public class CategoryAlreadyExistsException extends AlreadyExistsException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
