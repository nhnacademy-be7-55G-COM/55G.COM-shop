package shop.s5g.shop.repository.delivery.qdsl;

import java.util.Optional;
import shop.s5g.shop.entity.delivery.Delivery;

public interface DeliveryQuerydslRepository {

    Optional<Delivery> findByIdFetch(long orderId);

    Optional<Delivery> findByIdFetchAllDetails(long orderId);
}
