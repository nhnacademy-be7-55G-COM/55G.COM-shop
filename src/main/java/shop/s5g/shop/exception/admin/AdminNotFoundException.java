package shop.s5g.shop.exception.admin;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class AdminNotFoundException extends ResourceNotFoundException {

    public AdminNotFoundException(String message) {
        super(message);
    }
}
