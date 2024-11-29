package shop.s5g.shop.exception.review;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class ReviewNotFoundException extends ResourceNotFoundException {

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
