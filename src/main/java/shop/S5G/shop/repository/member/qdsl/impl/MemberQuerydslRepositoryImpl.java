package shop.S5G.shop.repository.member.qdsl.impl;

import static shop.S5G.shop.entity.member.QMember.member;

import java.time.LocalDateTime;
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

}
