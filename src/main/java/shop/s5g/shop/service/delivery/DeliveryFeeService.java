package shop.s5g.shop.service.delivery;

import java.util.List;
import shop.s5g.shop.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;

public interface DeliveryFeeService {

    List<DeliveryFeeResponseDto> getAllDeliveryFees();

    DeliveryFeeResponseDto createFee(DeliveryFeeCreateRequestDto createRequest);

    DeliveryFeeResponseDto updateFee(DeliveryFeeUpdateRequestDto updateRequest);
}
