package shop.S5G.shop.exception.member;

import shop.S5G.shop.exception.AlreadyExistsException;

public class MemberGradeAlreadyExistsException extends AlreadyExistsException {
    public MemberGradeAlreadyExistsException(String message) {
        super(message);
    }
}
