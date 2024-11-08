package shop.S5G.shop.dto.memberStatus;

import shop.S5G.shop.entity.member.MemberStatus;

public record MemberStatusResponseDto(
    Long memberStatusId,
    String typeName
) {

    public static MemberStatusResponseDto toDto(MemberStatus memberStatus) {
        return new MemberStatusResponseDto(memberStatus.getMemberStatusId(),
            memberStatus.getTypeName());
    }
}
