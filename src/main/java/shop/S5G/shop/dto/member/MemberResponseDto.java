package shop.S5G.shop.dto.member;

import java.time.LocalDateTime;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.entity.member.MemberStatus;

public record MemberResponseDto(
    Long customerId,
    MemberStatus status,
    MemberGrade grade,
    String loginId,
    String password,
    String birth,
    LocalDateTime createdAt,
    LocalDateTime latestLoginAt,
    Long point
) {

}
