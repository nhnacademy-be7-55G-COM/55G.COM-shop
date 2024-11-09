package shop.s5g.shop.repository.delivery.qdsl;

import java.util.Optional;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.shop.entity.delivery.DeliveryFee;

public interface DeliveryFeeQuerydslRepository {

    Optional<DeliveryFee> updateFee(DeliveryFeeUpdateRequestDto updateRequest);
}
