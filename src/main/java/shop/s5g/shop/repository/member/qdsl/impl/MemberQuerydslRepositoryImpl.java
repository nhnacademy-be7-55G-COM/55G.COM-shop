package shop.s5g.shop.repository.member.qdsl.impl;

import static shop.s5g.shop.entity.member.QCustomer.customer;
import static shop.s5g.shop.entity.member.QMember.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.repository.member.qdsl.MemberQuerydslRepository;

public class MemberQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    MemberQuerydslRepository {
    public MemberQuerydslRepositoryImpl() {
        super(Member.class);
    }

    @Deprecated
    @Override
    public Optional<Member> findByIdAndActiveIsTrue(long memberId) {
        return Optional.ofNullable(
            from(member)
                .innerJoin(member.customer, customer)
                .where(member.id.eq(memberId).and(customer.active.eq(true)))
                .fetchOne()
        );
    }
}
