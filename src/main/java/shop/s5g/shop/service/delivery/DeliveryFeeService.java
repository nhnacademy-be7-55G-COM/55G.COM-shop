package shop.s5g.shop.service.delivery;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;

public interface DeliveryFeeService {

    @Transactional(readOnly = true)
    List<DeliveryFeeResponseDto> getAllDeliveryFees();

    DeliveryFeeResponseDto createFee(DeliveryFeeCreateRequestDto createRequest);

    DeliveryFeeResponseDto updateFee(DeliveryFeeUpdateRequestDto updateRequest);
}
