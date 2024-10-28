package shop.S5G.shop.repository.order.qdsl.impl;

import static shop.S5G.shop.entity.QBook.book;
import static shop.S5G.shop.entity.order.QOrderDetail.orderDetail;
import static shop.S5G.shop.entity.order.QOrderDetailType.orderDetailType;
import static shop.S5G.shop.entity.order.QWrappingPaper.wrappingPaper;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.S5G.shop.entity.order.OrderDetail;
import shop.S5G.shop.repository.order.qdsl.OrderDetailQuerydslRepository;

public class OrderDetailQuerydslRepositoryImpl extends QuerydslRepositorySupport implements OrderDetailQuerydslRepository {
    public OrderDetailQuerydslRepositoryImpl() {
        super(OrderDetail.class);
    }

    @Override
    public List<OrderDetailWithBookResponseDto> queryAllDetailsByOrderId(long orderId) {
        return from(orderDetail)
            .innerJoin(orderDetail.book, book).fetchJoin()
            .innerJoin(orderDetail.wrappingPaper, wrappingPaper).fetchJoin()
            .innerJoin(orderDetail.orderDetailType, orderDetailType).fetchJoin()
//            .innerJoin(bookImage.bookId, book)
            .where(orderDetail.order.id.eq(orderId))
            .select(Projections.constructor(OrderDetailWithBookResponseDto.class,
                book.bookId,
                book.title,
                wrappingPaper.name,
                orderDetailType.name,
                orderDetail.quantity,
                orderDetail.totalPrice,
                orderDetail.accumulationPrice
                )
            ).fetch();
    }
}
