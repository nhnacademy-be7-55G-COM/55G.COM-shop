package shop.S5G.shop.exception.member;

import shop.S5G.shop.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
