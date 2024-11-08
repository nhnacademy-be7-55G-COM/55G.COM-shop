package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.AlreadyExistsException;

public class MemberStatusAlreadyExistsException extends AlreadyExistsException {

    public MemberStatusAlreadyExistsException(String message) {
        super(message);
    }
}
