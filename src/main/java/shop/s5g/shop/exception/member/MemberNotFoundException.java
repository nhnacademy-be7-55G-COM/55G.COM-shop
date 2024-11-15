package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
