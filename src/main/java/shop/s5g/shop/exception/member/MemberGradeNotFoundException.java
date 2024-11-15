package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class MemberGradeNotFoundException extends ResourceNotFoundException {
    public MemberGradeNotFoundException(String message) {
        super(message);
    }
}
