package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.AlreadyExistsException;

public class MemberAlreadyExistsException extends AlreadyExistsException {

    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}
