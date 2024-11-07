package shop.S5G.shop.repository.order.qdsl.impl;

import static shop.S5G.shop.entity.order.QDelivery.delivery;
import static shop.S5G.shop.entity.order.QDeliveryStatus.deliveryStatus;

import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.entity.order.Delivery;
import shop.S5G.shop.repository.order.qdsl.DeliveryQuerydslRepository;

public class DeliveryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements DeliveryQuerydslRepository {
    public DeliveryQuerydslRepositoryImpl() {
        super (Delivery.class);
    }

    @Override
    public Optional<Delivery> findByIdFetch(long orderId) {
        return Optional.ofNullable(
            from(delivery)
                .join(delivery.status, deliveryStatus).fetchJoin()
                .where(delivery.id.eq(orderId))
                .fetchOne()
        );
    }
}
