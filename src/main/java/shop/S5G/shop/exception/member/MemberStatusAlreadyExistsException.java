package shop.S5G.shop.exception.member;

import shop.S5G.shop.exception.AlreadyExistsException;

public class MemberStatusAlreadyExistsException extends AlreadyExistsException {

    public MemberStatusAlreadyExistsException(String message) {
        super(message);
    }
}
