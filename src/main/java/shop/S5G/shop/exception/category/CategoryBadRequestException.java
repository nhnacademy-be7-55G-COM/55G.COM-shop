package shop.S5G.shop.exception.category;

import shop.S5G.shop.exception.BadRequestException;

public class CategoryBadRequestException extends BadRequestException {
    public CategoryBadRequestException(String message) {
        super(message);
    }
}
