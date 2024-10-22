package shop.S5G.shop.exception.member;

import shop.S5G.shop.exception.BadRequestException;

public class MemberGradeBadRequestException extends BadRequestException {
    public MemberGradeBadRequestException(String message) {
        super(message);
    }
}
