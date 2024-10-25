package shop.S5G.shop.exception.member;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class MemberGradeNotFoundException extends ResourceNotFoundException {
    public MemberGradeNotFoundException(String message) {
        super(message);
    }
}
