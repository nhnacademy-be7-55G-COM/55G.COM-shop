package shop.s5g.shop.dto.delivery;

import java.time.LocalDate;
import shop.s5g.shop.entity.order.Delivery;

public record DeliveryResponseDto(
    long id,
    String address,
    LocalDate receivedDate,
    LocalDate shippingDate,
    int fee,
    String invoiceNumber,
    String receiverName,
    String status
) {
    public static DeliveryResponseDto of(Delivery delivery) {
        return new DeliveryResponseDto(
            delivery.getId(),
            delivery.getAddress(),
            delivery.getReceivedDate(),
            delivery.getShippingDate(),
            delivery.getFee(),
            delivery.getInvoiceNumber(),
            delivery.getReceiverName(),
            delivery.getStatus().getName()
        );
    }
}
