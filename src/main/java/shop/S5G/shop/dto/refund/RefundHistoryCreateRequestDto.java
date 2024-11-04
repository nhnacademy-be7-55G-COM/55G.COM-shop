package shop.S5G.shop.dto.refund;

public record RefundHistoryCreateRequestDto(long orderDetailId, String type, String reason) {
}
