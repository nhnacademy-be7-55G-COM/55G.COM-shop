package shop.S5G.shop.exception.publisher;

import shop.S5G.shop.exception.AlreadyExistsException;

public class PublisherAlreadyExistsException extends AlreadyExistsException {
    public PublisherAlreadyExistsException(String message) {
        super(message);
    }
}
