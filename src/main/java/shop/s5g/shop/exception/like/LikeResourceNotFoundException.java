package shop.s5g.shop.exception.like;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class LikeResourceNotFoundException extends ResourceNotFoundException {
    public LikeResourceNotFoundException(String message) {
        super(message);
    }
}
