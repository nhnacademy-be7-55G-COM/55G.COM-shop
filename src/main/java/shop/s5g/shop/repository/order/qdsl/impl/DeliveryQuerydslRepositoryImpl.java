package shop.s5g.shop.repository.order.qdsl.impl;

import static shop.s5g.shop.entity.order.QDelivery.delivery;
import static shop.s5g.shop.entity.order.QDeliveryStatus.deliveryStatus;
import static shop.s5g.shop.entity.order.QOrder.order;

import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.entity.order.Delivery;
import shop.s5g.shop.repository.order.qdsl.DeliveryQuerydslRepository;

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
