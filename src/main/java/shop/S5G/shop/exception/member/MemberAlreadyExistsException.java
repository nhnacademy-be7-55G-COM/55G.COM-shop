package shop.S5G.shop.exception.member;

import shop.S5G.shop.exception.AlreadyExistsException;

public class MemberAlreadyExistsException extends AlreadyExistsException {

    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}
