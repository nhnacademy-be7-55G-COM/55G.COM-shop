package shop.s5g.shop.exception.category;

import shop.s5g.shop.exception.BadRequestException;

public class CategoryBadRequestException extends BadRequestException {
    public CategoryBadRequestException(String message) {
        super(message);
    }
}
