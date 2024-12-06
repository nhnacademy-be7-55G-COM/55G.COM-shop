package shop.s5g.shop.dto.member_status;

import shop.s5g.shop.entity.member.MemberStatus;

public record MemberStatusResponseDto(
    Long memberStatusId,
    String typeName
) {

    public static MemberStatusResponseDto toDto(MemberStatus memberStatus) {
        return new MemberStatusResponseDto(memberStatus.getMemberStatusId(),
            memberStatus.getTypeName());
    }
}
