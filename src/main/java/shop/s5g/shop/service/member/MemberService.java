package shop.s5g.shop.service.member;


import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.member.IdCheckResponseDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member.MemberLoginIdResponseDto;
import shop.s5g.shop.dto.member.MemberRegistrationRequestDto;
import shop.s5g.shop.dto.member.MemberResponseDto;
import shop.s5g.shop.dto.member_status.MemberStatusResponseDto;
import shop.s5g.shop.entity.member.Member;

public interface MemberService {

    void saveMember(MemberRegistrationRequestDto memberRegistrationRequestDto);

    List<MemberResponseDto> findAllMembers(Pageable pageable);

    MemberDetailResponseDto getMemberDto(String loginId);

    Member getMember(String loginId);

    LoginResponseDto getLoginDto(String loginId);

    void deleteById(Long memberId);

    void updateLatestLoginAt(String loginId);

    IdCheckResponseDto isExistsByLoginId(String loginId);

    MemberLoginIdResponseDto getMemberLoginIdDtoByPaycoId(String paycoId);

    void changePassword(Long customerId, String oldPassword, String newPassword);

    void linkAccountByPaycoId(Long customerId, String paycoId);

    MemberStatusResponseDto getMemberStatusDtoByloginId(String loginId);

    void changeStatusToActive(String loginId);
}
