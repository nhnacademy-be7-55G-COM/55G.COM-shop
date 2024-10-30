package shop.S5G.shop.dto.payment.toss;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum TossPaymentsCardType {
    CREDIT("신용"),
    CHECK("체크"),
    GIFT("기프트"),
    UNKNOWN("미확인");

    private final String koreanName;
    TossPaymentsCardType(String name) {
        this.koreanName = name;
    }

    @JsonCreator
    public static TossPaymentsCardType fromString(String value) {
        for (TossPaymentsCardType cardType: TossPaymentsCardType.values()) {
            if (cardType.koreanName.equals(value)) {
                return cardType;
            }
        }
        throw new IllegalArgumentException();
    }
}
