package shop.s5g.shop.exception.member;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        super("회원이 존재하지 않습니다");
    }
}
