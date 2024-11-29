package shop.s5g.shop.exception.author;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class AuthorTypeResourceNotFoundException extends ResourceNotFoundException {

    public AuthorTypeResourceNotFoundException(String msg) {
        super(msg);
    }
}
