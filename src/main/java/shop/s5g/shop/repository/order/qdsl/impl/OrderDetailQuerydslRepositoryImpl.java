package shop.s5g.shop.repository.order.qdsl.impl;

import static shop.s5g.shop.entity.QBook.book;
import static shop.s5g.shop.entity.order.QOrderDetail.orderDetail;
import static shop.s5g.shop.entity.order.QOrder.order;
import static shop.s5g.shop.entity.order.QOrderDetailType.orderDetailType;
import static shop.s5g.shop.entity.order.QWrappingPaper.wrappingPaper;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.entity.order.OrderDetail;
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
}
