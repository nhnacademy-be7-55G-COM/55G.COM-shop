package shop.S5G.shop.dto.payment.toss;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;

public record TossPaymentsCancel(
    long cancelAmount,
    String cancelReason,
    long taxFreeAmount,
    int taxExceptionAmount,
    long refundableAmount,
    long easyPayDiscountAmount,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    LocalDateTime canceledAt,
    String transactionKey,
    @Nullable
    String receiptKey,
    String cancelSttus,
    @Nullable
    String cancelRequestId
) {

}
