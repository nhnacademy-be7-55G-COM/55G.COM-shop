package shop.S5G.shop.dto.member;

import java.time.LocalDateTime;
import shop.S5G.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.S5G.shop.dto.memberStatus.MemberStatusResponseDto;

public record MemberResponseDto(
    Long customerId,
    MemberStatusResponseDto status,
    MemberGradeResponseDto grade,
    String loginId,
    String password,
    String birth,
    LocalDateTime createdAt,
    LocalDateTime latestLoginAt,
    Long point
) {

}
