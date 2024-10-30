package shop.S5G.shop.dto.payment.toss;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum TossPaymentsOwnerType {
    PERSONAL("개인"),
    CORPORATION("법인"),
    UNKNOWN("미확인");

    private final String koreanName;
    TossPaymentsOwnerType(String name) {
        this.koreanName = name;
    }

    @JsonCreator
    public static TossPaymentsOwnerType fromString(String value) {
        for (TossPaymentsOwnerType cardType: TossPaymentsOwnerType.values()) {
            if (cardType.koreanName.equals(value)) {
                return cardType;
            }
        }
        throw new IllegalArgumentException();
    }
}
