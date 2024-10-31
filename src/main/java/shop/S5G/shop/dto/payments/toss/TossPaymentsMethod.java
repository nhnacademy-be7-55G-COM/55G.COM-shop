package shop.S5G.shop.dto.payments.toss;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum TossPaymentsMethod {
    CARD("카드"),
    VACCOUNT("가상계좌"),
    EASY_PAYMENT("간편결제"),
    MOBILE_PHONE("휴대폰"),
    TRANSFER("계좌이체"),
    CULTURE_LAND("문화상품권"),
    BOOK_CULTURE("도서문화상품권"),
    GAME_CULTURE("게임문화상품권");

    private final String koreanName;

    TossPaymentsMethod(String name) {
        koreanName = name;
    }

    @JsonCreator
    public static TossPaymentsMethod fromString(String value) {
        for (TossPaymentsMethod method: TossPaymentsMethod.values()) {
            if (method.koreanName.equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException();
    }
}
