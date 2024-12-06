package shop.s5g.shop.service.member;

import java.util.List;
import shop.s5g.shop.dto.member_status.MemberStatusRequestDto;
import shop.s5g.shop.dto.member_status.MemberStatusResponseDto;
import shop.s5g.shop.entity.member.MemberStatus;

public interface MemberStatusService {

    void saveMemberStatus(MemberStatusRequestDto memberStatusRequestDto);

    void updateMemberStatus(long memberStatusId, MemberStatusRequestDto memberStatusRequestDto);

    MemberStatusResponseDto getMemberStatus(long memberStatusId);

    List<MemberStatusResponseDto> getAllMemberStatus();

    MemberStatus getMemberStatusByTypeName(String typeName);

    void deleteMemberStatus(long memberStatusId);
}
