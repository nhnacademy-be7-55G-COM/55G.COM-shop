package shop.S5G.shop.dto.delivery;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DeliveryUpdateRequestDto(
    @Min(1)
    long id,

    @Size(min=1, max=100)
    String address,

    @Size(max=20)
    @Nullable
    String invoiceNumber,

    LocalDate receivedDate,

    LocalDate shippingDate,

    @Size(min=1, max=30)
    String receiverName,

    @NotNull
    String status
) {
}
