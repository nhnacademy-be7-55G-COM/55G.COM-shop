package shop.s5g.shop.repository.order.qdsl.impl;

import static shop.s5g.shop.entity.QBook.book;
import static shop.s5g.shop.entity.member.QCustomer.customer;
import static shop.s5g.shop.entity.order.QOrder.order;
import static shop.s5g.shop.entity.order.QOrderDetail.orderDetail;

import com.querydsl.core.types.Projections;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
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
}
