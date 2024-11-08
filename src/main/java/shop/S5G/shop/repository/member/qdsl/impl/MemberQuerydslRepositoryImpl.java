package shop.S5G.shop.repository.member.qdsl.impl;

import static shop.S5G.shop.entity.member.QCustomer.customer;
import static shop.S5G.shop.entity.member.QCustomer.customer;
import static shop.S5G.shop.entity.member.QMember.member;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.entity.member.Member;
import shop.S5G.shop.repository.member.qdsl.MemberQuerydslRepository;

public class MemberQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    MemberQuerydslRepository {

    public MemberQuerydslRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public void updateLatestLoginAt(String loginId, LocalDateTime lastLoginAt) {
        update(member)
            .set(member.latestLoginAt, lastLoginAt)
            .where(member.loginId.eq(loginId))
            .execute();
    }

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
