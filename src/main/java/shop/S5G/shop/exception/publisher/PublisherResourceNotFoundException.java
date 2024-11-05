package shop.S5G.shop.exception.publisher;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class PublisherResourceNotFoundException extends ResourceNotFoundException {
    public PublisherResourceNotFoundException(String message) {
        super(message);
    }
}
