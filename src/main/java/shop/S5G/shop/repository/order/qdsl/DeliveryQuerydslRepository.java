package shop.S5G.shop.repository.order.qdsl;

import java.util.Optional;
import shop.S5G.shop.entity.order.Delivery;

public interface DeliveryQuerydslRepository {

    Optional<Delivery> findByIdFetch(long orderId);
}
