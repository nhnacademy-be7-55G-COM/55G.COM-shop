package shop.S5G.shop.repository.order.qdsl.impl;

import static shop.S5G.shop.entity.QBook.book;
import static shop.S5G.shop.entity.order.QOrder.order;
import static shop.S5G.shop.entity.order.QOrderDetail.orderDetail;

import com.querydsl.core.types.Projections;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.entity.order.Order;
import shop.S5G.shop.repository.order.qdsl.OrderQuerydslRepository;

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
            )
            .groupBy(order.id)
//            .offset(pageable.getOffset())
//            .limit(pageable.getPageSize())
            .orderBy(order.id.desc())
            .select(Projections.constructor(OrderWithDetailResponseDto.class,
                order.id, order.orderedAt, order.netPrice, order.totalPrice,
                orderDetail.book.title.min(), order.id.count(), orderDetail.quantity.sum())
            ).fetch();
    }

//
//        Long count = Objects.requireNonNull(
//            from(order)
//            .where(order.customer.customerId.eq(id))
//            .select(order.count())
//            .fetchOne()     // fetchCount()는 5.x 버전에서 deprecated.
//        );

//        return new PageImpl<>(list, fromDate, count);
//        return List.of();

    @Override
    public List<OrderWithDetailResponseDto> findOrdersByCustomerId(long id) {
        return from(order)
            .innerJoin(order.orderDetails, orderDetail)
            .innerJoin(orderDetail.book, book)
            .where(order.customer.customerId.eq(id))
            .groupBy(order.id)
            .orderBy(order.id.desc())
            .select(Projections.constructor(OrderWithDetailResponseDto.class,
                order.id, order.orderedAt, order.netPrice, order.totalPrice,
                orderDetail.book.title.min(), order.id.count(), orderDetail.quantity.sum())
            ).fetch();
    }
}
