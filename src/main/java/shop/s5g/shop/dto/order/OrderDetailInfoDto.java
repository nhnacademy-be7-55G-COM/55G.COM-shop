package shop.s5g.shop.dto.order;

import java.util.List;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;

public record OrderDetailInfoDto(
    List<OrderDetailWithBookResponseDto> details,
    DeliveryResponseDto delivery,
    List<RefundHistoryResponseDto> refunds
) {

}
