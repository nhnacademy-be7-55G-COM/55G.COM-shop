package shop.s5g.shop.dto.refund;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RefundHistoryCreateRequestDto(
    @Min(1)
    long orderDetailId,
    @Min(1)
    long typeId,
    @Size(min=1)
    @NotNull
    String reason,
    @Nullable
    List<String> images
) {
}
