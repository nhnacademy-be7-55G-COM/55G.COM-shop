package shop.S5G.shop.exception.tag;

import shop.S5G.shop.exception.BadRequestException;

public class TagBadRequestException extends BadRequestException {
    public TagBadRequestException(String message) {
        super(message);
    }
}
