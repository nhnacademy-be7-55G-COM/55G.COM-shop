package shop.s5g.shop.exception.review;

import shop.s5g.shop.exception.AlreadyExistsException;

public class ReviewAlreadyExistsException extends AlreadyExistsException {

    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
