package shop.S5G.shop.exception.tag;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class TagResourceNotFoundException extends ResourceNotFoundException {
    public TagResourceNotFoundException(String message) {
        super(message);
    }
}
