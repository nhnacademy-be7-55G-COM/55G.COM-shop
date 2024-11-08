package shop.s5g.shop.exception.publisher;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class PublisherResourceNotFoundException extends ResourceNotFoundException {
    public PublisherResourceNotFoundException(String message) {
        super(message);
    }
}
