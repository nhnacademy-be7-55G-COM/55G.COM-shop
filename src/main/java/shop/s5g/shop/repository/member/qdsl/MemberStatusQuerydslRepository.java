package shop.s5g.shop.repository.member.qdsl;

import shop.s5g.shop.dto.member_status.MemberStatusRequestDto;

public interface MemberStatusQuerydslRepository {

    void updateMemberStatus(long memberStatusId, MemberStatusRequestDto memberStatusRequestDto);
}
