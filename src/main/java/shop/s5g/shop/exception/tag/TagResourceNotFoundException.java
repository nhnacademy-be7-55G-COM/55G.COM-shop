package shop.s5g.shop.exception.tag;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class TagResourceNotFoundException extends ResourceNotFoundException {
    public TagResourceNotFoundException(String message) {
        super(message);
    }
}
