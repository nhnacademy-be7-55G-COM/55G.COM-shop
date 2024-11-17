package shop.s5g.shop.repository.order.qdsl.impl;

import static shop.s5g.shop.entity.QBook.book;
import static shop.s5g.shop.entity.QPayment.payment;
import static shop.s5g.shop.entity.delivery.QDelivery.delivery;
import static shop.s5g.shop.entity.delivery.QDeliveryStatus.deliveryStatus;
import static shop.s5g.shop.entity.member.QCustomer.customer;
import static shop.s5g.shop.entity.order.QOrder.order;
import static shop.s5g.shop.entity.order.QOrderDetail.orderDetail;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.order.OrderAdminTableView;
import shop.s5g.shop.dto.order.OrderQueryFilterDto;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.repository.order.qdsl.OrderQuerydslRepository;

public class OrderQuerydslRepositoryImpl extends QuerydslRepositorySupport
    implements OrderQuerydslRepository {
    public OrderQuerydslRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public List<OrderWithDetailResponseDto> findOrdersByCustomerIdBetweenDates(long id, LocalDate fromDate,
        LocalDate toDate) {

        return from(order)
            .innerJoin(order.orderDetails, orderDetail)
            .innerJoin(orderDetail.book, book)
            .where(
                order.customer.customerId.eq(id)
                    .and(order.orderedAt.before(toDate.atTime(23, 59, 59, 999))
                    .and(order.orderedAt.after(fromDate.atTime(0, 0))))
                    .and(order.active.eq(true))
            )
            .groupBy(order.id)
            .orderBy(order.id.desc())
            .select(Projections.constructor(OrderWithDetailResponseDto.class,
                order.id, order.orderedAt, order.netPrice, order.totalPrice,
                orderDetail.book.title.min(), order.id.count(), orderDetail.quantity.sum())
            ).fetch();
    }

    @Override
    public List<OrderWithDetailResponseDto> findOrdersByCustomerId(long id) {
        return from(order)
            .innerJoin(order.orderDetails, orderDetail)
            .innerJoin(orderDetail.book, book)
            .where(order.customer.customerId.eq(id).and(order.active.eq(true)))
            .groupBy(order.id)
            .orderBy(order.id.desc())
            .select(Projections.constructor(OrderWithDetailResponseDto.class,
                order.id, order.orderedAt, order.netPrice, order.totalPrice,
                orderDetail.book.title.min(), order.id.count(), orderDetail.quantity.sum())
            ).fetch();
    }

    @Override
    public Order findOrderByIdFetch(long orderId) {
        return from(order)
            .join(order.customer, customer).fetchJoin()
            .where(order.id.eq(orderId))
            .fetchOne();
    }

    @Override
    public List<OrderAdminTableView> findOrdersByCustomerIdForAdmin(long id) {
        return from(order)
            .innerJoin(order.customer, customer)
            .leftJoin(payment).on(order.id.eq(payment.orderRelation.id))
            .innerJoin(order.delivery, delivery)
            .innerJoin(delivery.status, deliveryStatus)
            .where(customer.customerId.eq(id))
            .orderBy(order.orderedAt.desc())
            .select(Projections.constructor(OrderAdminTableView.class,
                order.id,
                order.totalPrice,
                payment.amount,
                deliveryStatus.name,
                order.orderedAt,
                order.active
                )
            ).fetch();
    }
    @Override
    public List<OrderAdminTableView> findOrdersUsingFilterForAdmin(OrderQueryFilterDto filter) {
        if (filter.orderId() != null) {
            // 어차피 하나밖에 없음...
            return findOrdersByCustomerIdForAdmin(filter.orderId());
        }
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<Order> qf =
            from(order)
            .innerJoin(order.customer, customer)
            .leftJoin(payment).on(order.id.eq(payment.orderRelation.id))
            .innerJoin(order.delivery, delivery)
            .innerJoin(delivery.status, deliveryStatus);
        if (filter.active() != null) {
            builder.and(order.active.eq(filter.active()));
        }
        if (filter.customerId() != null) {
            builder.and(customer.customerId.eq(filter.customerId()));
        }
        if (filter.deliveryStatus() != null && !filter.deliveryStatus().isEmpty()) {
            builder.and(deliveryStatus.name.eq(filter.deliveryStatus()));
        }
        if (filter.page() != null && filter.size() != null) {
            qf = qf.limit(filter.size()).offset((long) (filter.page()-1)*filter.size());
        }
        return qf
            .where(builder)
            .orderBy(order.orderedAt.desc())
            .select(Projections.constructor(OrderAdminTableView.class,
                    order.id,
                    order.totalPrice,
                    payment.amount,
                    deliveryStatus.name,
                    order.orderedAt,
                    order.active
                )
            ).fetch();
    }
}
