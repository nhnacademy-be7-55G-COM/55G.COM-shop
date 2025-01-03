package shop.s5g.shop.repository.delivery.qdsl.impl;

import static shop.s5g.shop.entity.delivery.QDelivery.delivery;
import static shop.s5g.shop.entity.delivery.QDeliveryStatus.deliveryStatus;
import static shop.s5g.shop.entity.order.QOrder.order;
import static shop.s5g.shop.entity.order.QOrderDetail.orderDetail;
import static shop.s5g.shop.entity.order.QOrderDetailType.orderDetailType;

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
                .join(delivery.order, order)
                .where(order.id.eq(orderId))
                .fetchOne()
        );
    }

    @Override
    public Optional<Delivery> findByIdFetchAllDetails(long orderId) {
        return Optional.ofNullable(
            from(delivery)
                .join(delivery.status, deliveryStatus).fetchJoin()
                .join(delivery.order, order).fetchJoin()
                .join(order.orderDetails, orderDetail).fetchJoin()
                .join(orderDetail.orderDetailType, orderDetailType).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchFirst()
        );
    }
}
