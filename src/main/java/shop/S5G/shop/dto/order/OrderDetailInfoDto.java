package shop.S5G.shop.dto.order;

import java.util.List;
import shop.S5G.shop.dto.delivery.DeliveryResponseDto;
import shop.S5G.shop.dto.refund.RefundHistoryResponseDto;

public record OrderDetailInfoDto(
    List<OrderDetailWithBookResponseDto> details,
    DeliveryResponseDto delivery,
    List<RefundHistoryResponseDto> refunds
) {

}
