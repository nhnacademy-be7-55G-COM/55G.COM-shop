package shop.s5g.shop.repository.order.qdsl.impl;

import static shop.s5g.shop.entity.QBook.book;
import static shop.s5g.shop.entity.order.QOrderDetail.orderDetail;
import static shop.s5g.shop.entity.refund.QRefundHistory.refundHistory;
import static shop.s5g.shop.entity.refund.QRefundType.refundType;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;
import shop.s5g.shop.entity.refund.RefundHistory;
import shop.s5g.shop.repository.order.qdsl.RefundHistoryQuerydslRepository;

public class RefundHistoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements RefundHistoryQuerydslRepository {
    public RefundHistoryQuerydslRepositoryImpl() {
        super(RefundHistory.class);
    }

    @Override
    public RefundHistoryResponseDto fetchRefundHistoryByOrderDetailId(long orderDetailId) {
        return from(refundHistory)
            .innerJoin(refundHistory.orderDetail, orderDetail)
            .innerJoin(refundHistory.refundType, refundType)
            .innerJoin(orderDetail.book, book)
            .where(orderDetail.id.eq(orderDetailId))
            .select(Projections.constructor(RefundHistoryResponseDto.class,
                orderDetail.id,
                book.title,
                orderDetail.quantity,
                refundType.name,
                refundHistory.reason,
                refundHistory.refundedAt
            )).fetchOne();
    }
}
