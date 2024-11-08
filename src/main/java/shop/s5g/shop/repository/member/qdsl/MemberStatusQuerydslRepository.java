package shop.s5g.shop.repository.member.qdsl;

import shop.s5g.shop.dto.memberStatus.MemberStatusRequestDto;

public interface MemberStatusQuerydslRepository {

    void updateMemberStatus(long memberStatusId, MemberStatusRequestDto memberStatusRequestDto);
}
