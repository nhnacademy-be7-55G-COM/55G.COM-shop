package shop.s5g.shop.dto.payments;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import shop.s5g.shop.dto.payments.toss.*;

// https://docs.tosspayments.com/reference
public record TossPaymentsDto(
    String version,
    @Size(max = 200)
    String paymentKey,
    TossPaymentsType type,
    @Size(min = 6, max = 64)
    String orderId,
    @Size(max = 100)
    String orderName,
    @Size(max = 14)
    String mId,
    String currency,
    @Nullable
    TossPaymentsMethod method,
    long totalAmount,
    long balanceAmount,
    TossPaymentsStatus status,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    LocalDateTime requestedAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Nullable
    LocalDateTime approvedAt,
    boolean useEscrow,
    @Size(max=64)
    @Nullable
    String lastTransactionKey,
    long suppliedAmount,
    long vat,
    boolean cultureExpense,
    long taxFreeAmount,
    int taxExemptionAmount,
    @Nullable
    List<TossPaymentsCancel> cancels,
    boolean isPartialCancelable,
    @Nullable
    TossPaymentsCard card,
    @Nullable
    TossPaymentsVirtualAccount virtualAccount,
    @Size(max = 50)
    @Nullable
    String secret,
    @Nullable
    TossPaymentsMobilePhone mobilePhone,
    @Nullable
    TossPaymentsGiftCertificate giftCertificate,
    @Nullable
    TossPaymentsTransfer transfer,
    Map<String, Object> metadata,
    @Nullable
    TossPaymentsReceipt receipt,
    @Nullable
    TossPaymentsCheckout checkout,
    @Nullable
    TossPaymentsEasyPay easyPay,
    String country,
    @Nullable
    TossPaymentsFailure failure,
    @Nullable
    TossPaymentsCashReceipt cashReceipt,
    @Nullable
    List<Map<String, Object>> cashReceipts, // TODO: API 다시 확인..
    @Nullable
    TossPaymentsDiscount discount
) {

}
