package shop.S5G.shop.dto.payments.toss;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;

public record TossPaymentsVirtualAccount(
    String accountType, // TODO: 일반, 고정
    @Size(max = 20)
    String accountNumber,
    String bankCode,
    @Size(max = 100)
    String customerName,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dueDate,
    TossPaymentsRefundStatus refundStatus,
    boolean expired,
    String settlementStatus,
    @Nullable
    TossPaymentsRefundReceiveAccount refundReceiveAccount
) {

}
