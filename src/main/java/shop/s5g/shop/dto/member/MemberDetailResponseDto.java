package shop.s5g.shop.dto.member;

import java.time.LocalDateTime;
import java.util.List;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.member_grade.MemberGradeResponseDto;
import shop.s5g.shop.dto.member_status.MemberStatusResponseDto;

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
    Long point,
    List<AddressResponseDto> addresses
) {

}
