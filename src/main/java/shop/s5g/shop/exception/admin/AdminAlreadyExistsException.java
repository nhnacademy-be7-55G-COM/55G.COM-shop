package shop.s5g.shop.exception.admin;

import shop.s5g.shop.exception.AlreadyExistsException;

public class AdminAlreadyExistsException extends AlreadyExistsException {

    public AdminAlreadyExistsException(String message) {
        super(message);
    }
}
