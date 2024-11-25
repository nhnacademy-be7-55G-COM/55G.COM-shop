package shop.s5g.shop.repository.order.qdsl.impl;

import static shop.s5g.shop.entity.QBook.book;
import static shop.s5g.shop.entity.delivery.QDelivery.delivery;
import static shop.s5g.shop.entity.delivery.QDeliveryFee.deliveryFee;
import static shop.s5g.shop.entity.order.QOrder.order;
import static shop.s5g.shop.entity.order.QOrderDetail.orderDetail;
import static shop.s5g.shop.entity.order.QOrderDetailType.orderDetailType;
import static shop.s5g.shop.entity.order.QWrappingPaper.wrappingPaper;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.dto.refund.OrderDetailDeliveryPairContainer;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.repository.order.qdsl.OrderDetailQuerydslRepository;

public class OrderDetailQuerydslRepositoryImpl extends QuerydslRepositorySupport implements OrderDetailQuerydslRepository {
    public OrderDetailQuerydslRepositoryImpl() {
        super(OrderDetail.class);
    }

    @Override
    public List<OrderDetailWithBookResponseDto> queryAllDetailsByOrderId(long orderId) {
        return from(orderDetail)
            .innerJoin(orderDetail.book, book)
            .leftJoin(orderDetail.wrappingPaper, wrappingPaper)
            .innerJoin(orderDetail.orderDetailType, orderDetailType)
//            .innerJoin(bookImage.bookId, book)
            .where(orderDetail.order.id.eq(orderId))
            .select(Projections.constructor(OrderDetailWithBookResponseDto.class,
                book.bookId,
                book.title,
                wrappingPaper.name,
                orderDetail.id,
                orderDetailType.name,
                orderDetail.quantity,
                orderDetail.totalPrice,
                orderDetail.accumulationPrice
                )
            ).fetch();
    }

    @Override
    public List<OrderDetail> fetchOrderDetailsByOrderId(long orderId) {
        return from(orderDetail)
            .join(orderDetail.book, book).fetchJoin()
            .join(orderDetail.order, order).fetchJoin()
            .where(orderDetail.order.id.eq(orderId))
            .fetch();
    }

    /**
     * orderDetailType, order, deliveryFee 페치조인
     * @param orderDetailId
     * @return orderDetailId에 해당하는 OrderDetail과 Delivery 페어 리턴.
     */
    @Override
    @SuppressWarnings("unchecked")
    public OrderDetailDeliveryPairContainer fetchOrderDetailAndDelivery(long orderDetailId) {
        try {
            OrderDetail orderDetail1 = Objects.requireNonNull(
                from(orderDetail)
                    .join(orderDetail.orderDetailType, orderDetailType).fetchJoin()
                    .join(orderDetail.order, order).fetchJoin()
                    .join(order.delivery, delivery).fetchJoin()
                    .join(delivery.deliveryFee, deliveryFee).fetchJoin()
                    .where(orderDetail.id.eq(orderDetailId).and(order.active.eq(true)))
                    .fetchOne()
            );

            return new OrderDetailDeliveryPairContainer(
                orderDetail1, orderDetail1.getOrder().getDelivery()
            );
        }catch (IllegalArgumentException | NullPointerException e) {
            throw new OrderDetailsNotExistException("환불 대상이 존재하지 않거나 해당되는 배송이 존재하지 않습니다.");
        }
    }
}
