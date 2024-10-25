package shop.S5G.shop.service.member;

import java.util.List;
import shop.S5G.shop.dto.memberStatus.MemberStatusRequestDto;
import shop.S5G.shop.dto.memberStatus.MemberStatusResponseDto;
import shop.S5G.shop.entity.member.MemberStatus;

public interface MemberStatusService {

    void saveMemberStatus(MemberStatusRequestDto memberStatusRequestDto);

    void updateMemberStatus(long memberStatusId, MemberStatusRequestDto memberStatusRequestDto);

    MemberStatusResponseDto getMemberStatus(long memberStatusId);

    List<MemberStatusResponseDto> getAllMemberStatus();

    MemberStatus getMemberStatusByTypeName(String typeName);

    void deleteMemberStatus(long memberStatusId);

}
