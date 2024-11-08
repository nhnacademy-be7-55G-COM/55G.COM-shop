package shop.S5G.shop.repository.point.qdsl.impl;

import static shop.S5G.shop.entity.member.QCustomer.customer;
import static shop.S5G.shop.entity.member.QMember.member;
import static shop.S5G.shop.entity.point.QPointHistory.pointHistory;
import static shop.S5G.shop.entity.point.QPointSource.pointSource;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.point.PointHistoryResponseDto;
import shop.S5G.shop.entity.point.PointHistory;
import shop.S5G.shop.repository.point.qdsl.PointHistoryQuerydslRepository;

public class PointHistoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements PointHistoryQuerydslRepository {
    public PointHistoryQuerydslRepositoryImpl() {
        super(PointHistory.class);
    }

     @Override
     public Page<PointHistoryResponseDto> findPointHistoryByMemberId(long memberId, Pageable pageable) {
        List<PointHistoryResponseDto> list = from(pointHistory)
            .innerJoin(pointHistory.member, member)
            .innerJoin(pointHistory.pointSource, pointSource)
            .innerJoin(member.customer, customer)
            .where(customer.customerId.eq(memberId).and(pointHistory.active.eq(true)))
            .orderBy(pointHistory.id.desc())
            .offset(pageable.getOffset()).limit(pageable.getPageSize())
            .select(Projections.constructor(
                PointHistoryResponseDto.class,
                pointHistory.id,
                pointSource.sourceName,
                pointHistory.point,
                pointHistory.remainingPoint,
                pointHistory.createdAt
            ))
            .fetch();

        Long count = Objects.requireNonNull(
            from(pointHistory)
                .where(
                    pointHistory.member.id.eq(memberId)
                        .and(pointHistory.active.eq(true))
                )
                .select(pointHistory.count())
                .fetchOne()
        );

        return new PageImpl<>(list, pageable, count);
     }
}
