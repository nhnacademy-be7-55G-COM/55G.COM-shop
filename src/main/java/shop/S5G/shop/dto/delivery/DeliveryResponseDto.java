package shop.S5G.shop.dto.delivery;

import java.time.LocalDate;
import shop.S5G.shop.entity.order.Delivery;

public record DeliveryResponseDto(
    long id,
    String address,
    LocalDate receivedDate,
    LocalDate shippingDate,
    int fee,
    String invoiceNumber
) {
    public static DeliveryResponseDto of(Delivery delivery) {
        return new DeliveryResponseDto(
            delivery.getId(),
            delivery.getAddress(),
            delivery.getReceivedDate(),
            delivery.getShippingDate(),
            delivery.getFee(),
            delivery.getInvoiceNumber()
        );
    }
}
