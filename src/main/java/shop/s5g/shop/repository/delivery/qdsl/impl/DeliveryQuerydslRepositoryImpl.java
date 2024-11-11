package shop.s5g.shop.repository.delivery.qdsl.impl;

import static shop.s5g.shop.entity.delivery.QDelivery.delivery;
import static shop.s5g.shop.entity.delivery.QDeliveryStatus.deliveryStatus;
import static shop.s5g.shop.entity.order.QOrder.order;

import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.repository.delivery.qdsl.DeliveryQuerydslRepository;

public class DeliveryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements DeliveryQuerydslRepository {
    public DeliveryQuerydslRepositoryImpl() {
        super (Delivery.class);
    }

    @Override
    public Optional<Delivery> findByIdFetch(long orderId) {
        return Optional.ofNullable(
            from(delivery)
                .join(delivery.status, deliveryStatus).fetchJoin()
                .join(order.delivery, delivery)
                .where(order.id.eq(orderId))
                .fetchOne()
        );
    }
}
