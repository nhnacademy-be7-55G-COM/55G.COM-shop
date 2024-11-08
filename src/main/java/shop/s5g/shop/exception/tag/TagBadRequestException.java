package shop.s5g.shop.exception.tag;

import shop.s5g.shop.exception.BadRequestException;

public class TagBadRequestException extends BadRequestException {
    public TagBadRequestException(String message) {
        super(message);
    }
}
