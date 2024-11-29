package shop.s5g.shop.exception.like;

import shop.s5g.shop.exception.BadRequestException;

public class LikeBadRequestException extends BadRequestException {
    public LikeBadRequestException(String message) {
        super(message);
    }
}
