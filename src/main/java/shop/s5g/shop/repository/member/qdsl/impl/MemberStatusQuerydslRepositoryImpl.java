package shop.s5g.shop.repository.member.qdsl.impl;

import static shop.s5g.shop.entity.member.QMemberStatus.memberStatus;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.member_status.MemberStatusRequestDto;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.repository.member.qdsl.MemberStatusQuerydslRepository;

public class MemberStatusQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    MemberStatusQuerydslRepository {

    public MemberStatusQuerydslRepositoryImpl() {
        super(MemberStatus.class);
    }

    @Override
    public void updateMemberStatus(long memberStatusId, MemberStatusRequestDto requestDto) {
        update(memberStatus)
            .set(memberStatus.typeName, requestDto.typeName())
            .where(memberStatus.memberStatusId.eq(memberStatusId))
            .execute();
    }
}
