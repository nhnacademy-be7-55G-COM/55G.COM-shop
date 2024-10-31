package shop.S5G.shop.dto.delivery;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 새로운 배송을 생성하는 DTO
 * @param address   배달 주소
 * @param deliveryFeeId 배송비 id
 * @param receivedDate  언제 받고싶은지?
 */
public record DeliveryCreateRequestDto(
    @Size(min=1, max=100)
    String address,
    @Min(1)
    long deliveryFeeId,
    @FutureOrPresent
    LocalDate receivedDate) {
    // 출고일, 배송비, 송장번호는 서비스레이어가 생성할 것.
}
