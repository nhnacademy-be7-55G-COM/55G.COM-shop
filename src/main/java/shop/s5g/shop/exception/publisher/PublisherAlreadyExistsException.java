package shop.s5g.shop.exception.publisher;

import shop.s5g.shop.exception.AlreadyExistsException;

public class PublisherAlreadyExistsException extends AlreadyExistsException {
    public PublisherAlreadyExistsException(String message) {
        super(message);
    }
}
