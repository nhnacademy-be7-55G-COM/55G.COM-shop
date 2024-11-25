package shop.s5g.shop.dto.member;

import java.time.LocalDateTime;
import shop.s5g.shop.dto.member_grade.MemberGradeResponseDto;
import shop.s5g.shop.dto.member_status.MemberStatusResponseDto;

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
