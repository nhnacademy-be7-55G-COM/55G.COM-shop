package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class MemberStatusNotFoundException extends ResourceNotFoundException {

    public MemberStatusNotFoundException(String message) {
        super(message);
    }
}
