package shop.S5G.shop.repository.order.qdsl.impl;

import static com.querydsl.jpa.JPAExpressions.select;
import static shop.S5G.shop.entity.QBook.book;
import static shop.S5G.shop.entity.order.QOrder.order;
import static shop.S5G.shop.entity.order.QOrderDetail.orderDetail;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<OrderWithDetailResponseDto> findOrdersByCustomerId(long id, Pageable pageable) {
//        select()from(order).join(orderDetail).on(orderDetail.order.id.eq(order.id)).fetch()
        List<OrderWithDetailResponseDto> list = from(order)
            .join(order.orderDetails, orderDetail).fetchJoin()
            .join(orderDetail.book, book).fetchJoin()
            .where(order.customer.customerId.eq(id))
            .groupBy(order.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .select(Projections.constructor(OrderWithDetailResponseDto.class,
                order.id, order.orderedAt, order.netPrice, order.totalPrice, orderDetail.book.title.min(), order.id.count(), orderDetail.quantity.sum())
            ).fetch();

        Long count = Objects.requireNonNull(
            select(order.count())
            .from(order)
            .where(order.customer.customerId.eq(id))
            .fetchOne()     // fetchCount()는 5.x 버전에서 deprecated.
        );

        return new PageImpl<>(list, pageable, count);
//        return List.of();
    }
}
