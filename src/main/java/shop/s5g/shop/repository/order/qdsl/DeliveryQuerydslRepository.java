package shop.s5g.shop.repository.order.qdsl;

import java.util.Optional;
import shop.s5g.shop.entity.order.Delivery;

public interface DeliveryQuerydslRepository {

    Optional<Delivery> findByIdFetch(long orderId);
}
