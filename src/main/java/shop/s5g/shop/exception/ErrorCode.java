package shop.s5g.shop.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DISCOUNT_PRICE_NEGATIVE_OR_ZERO("할인 금액은 음수나 0일 수 없습니다."),
    CONDITION_OUT_OF_RANGE("조건 금액은 10,000원 이상 500,000원 이하만 가능합니다."),
    DISCOUNT_EXCEEDS_80_PERCENT("할인은 최대 80%를 초과할 수 없습니다."),
    DISCOUNT_INVALID_RANGE("할인은 최소 1,000원 이상, 조건 금액의 80% 이하여야 합니다."),
    MAX_PRICE_EXCEEDS_LIMIT("최대 할인은 조건 금액과 할인률을 나눈 가격까지만 측정 가능합니다."),
    DURATION_OUT_OF_RANGE("할인 기간은 1일 이상 365일 이하여야 합니다."),

    COUPON_TEMPLATE_NOT_FOUND("쿠폰 템플릿이 존재하지 않습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
