package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.AlreadyExistsException;

public class MemberGradeAlreadyExistsException extends AlreadyExistsException {
    public MemberGradeAlreadyExistsException(String message) {
        super(message);
    }
}
