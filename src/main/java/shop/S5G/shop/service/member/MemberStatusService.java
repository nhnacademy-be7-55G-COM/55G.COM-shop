package shop.S5G.shop.service.member;

import shop.S5G.shop.dto.memberStatus.MemberStatusRequestDto;
import shop.S5G.shop.entity.member.MemberStatus;

public interface MemberStatusService {
    void saveMemberStatus(MemberStatusRequestDto memberStatusRequestDto);
    void updateMemberStatus(MemberStatusRequestDto memberStatusRequestDto);
}
