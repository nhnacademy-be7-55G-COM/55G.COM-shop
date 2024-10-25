package shop.S5G.shop.repository.member.qdsl;

import shop.S5G.shop.dto.memberStatus.MemberStatusRequestDto;

public interface MemberStatusQuerydslRepository {

    void updateMemberStatus(long memberStatusId, MemberStatusRequestDto memberStatusRequestDto);
}
