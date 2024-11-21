package shop.s5g.shop.exception.author;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class AuthorResourceNotFooundException extends ResourceNotFoundException {
    public AuthorResourceNotFooundException(String message) {
        super(message);
    }
}
