package shop.s5g.shop.exception.like;

import shop.s5g.shop.exception.AlreadyExistsException;

public class LikeAlreadyExistsException extends AlreadyExistsException {
    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
