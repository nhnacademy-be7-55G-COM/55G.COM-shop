package shop.s5g.shop.exception.category;

import shop.s5g.shop.exception.AlreadyExistsException;

public class CategoryAlreadyExistsException extends AlreadyExistsException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
