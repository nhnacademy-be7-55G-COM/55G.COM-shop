package shop.s5g.shop.dto.delivery;

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
// TODO: 이걸 사용하는 front를 리팩토링
public record DeliveryCreateRequestDto(
    @Size(min=1, max=100)
    String address,
    @Min(1)
    long deliveryFeeId,
    @FutureOrPresent
    LocalDate receivedDate,
    @Size(max=30)
    String receiverName
) {
    // 출고일, 배송비, 송장번호는 서비스레이어가 생성할 것.
}
