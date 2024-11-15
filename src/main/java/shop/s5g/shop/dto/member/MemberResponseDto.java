package shop.s5g.shop.dto.member;

import java.time.LocalDateTime;
import shop.s5g.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.s5g.shop.dto.memberStatus.MemberStatusResponseDto;

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
