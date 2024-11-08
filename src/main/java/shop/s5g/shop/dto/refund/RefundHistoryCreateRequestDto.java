package shop.s5g.shop.dto.refund;

public record RefundHistoryCreateRequestDto(long orderDetailId, String type, String reason) {
}
