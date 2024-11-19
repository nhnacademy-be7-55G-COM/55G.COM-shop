package shop.s5g.shop.dto.delivery;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record DeliveryUpdateRequestDto(
    @Min(1) // order ID
    long id,

    @Size(min=1, max=100)
    String address,

    @Size(max=20)
    @Nullable
    String invoiceNumber,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    @NotNull
    LocalDate receivedDate,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    LocalDate shippingDate,

    @Size(min=1, max=30)
    String receiverName,

    @NotNull
    String status
) {
}
