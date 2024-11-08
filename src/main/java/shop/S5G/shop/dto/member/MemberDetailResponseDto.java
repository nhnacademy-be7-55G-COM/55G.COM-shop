package shop.S5G.shop.dto.member;

import java.time.LocalDateTime;
import shop.S5G.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.S5G.shop.dto.memberStatus.MemberStatusResponseDto;

public record MemberDetailResponseDto(
    Long customerId,
    MemberStatusResponseDto status,
    MemberGradeResponseDto grade,
    String loginId,
    String password,
    String birth,
    String name,
    String email,
    String phoneNumber,
    LocalDateTime createdAt,
    LocalDateTime latestLoginAt,
    Long point
) {

}
