package shop.S5G.shop.exception.member;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class MemberStatusNotFoundException extends ResourceNotFoundException {

    public MemberStatusNotFoundException(String message) {
        super(message);
    }
}
