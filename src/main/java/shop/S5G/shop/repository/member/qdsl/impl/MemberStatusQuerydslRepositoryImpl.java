package shop.S5G.shop.repository.member.qdsl.impl;

import static shop.S5G.shop.entity.member.QMemberStatus.memberStatus;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.entity.member.MemberStatus;
import shop.S5G.shop.repository.member.qdsl.MemberStatusQuerydslRepository;

public class MemberStatusQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    MemberStatusQuerydslRepository {

    public MemberStatusQuerydslRepositoryImpl() {
        super(MemberStatus.class);
    }

    @Override
    public void updateMemberStatus(long memberStatusId, String typeName) {
        update(memberStatus)
            .set(memberStatus.typeName, typeName)
            .where(memberStatus.memberStatusId.eq(memberStatusId))
            .execute();
    }
}
