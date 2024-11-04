package shop.S5G.shop.service.member;


import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.member.LoginResponseDto;
import shop.S5G.shop.dto.member.MemberRegistrationRequestDto;
import shop.S5G.shop.dto.member.MemberResponseDto;
import shop.S5G.shop.dto.member.MemberUpdateRequestDto;
import shop.S5G.shop.entity.member.Member;

public interface MemberService {

    void saveMember(MemberRegistrationRequestDto memberRegistrationRequestDto);

    void updateMember(MemberUpdateRequestDto memberUpdateRequestDto);

    List<MemberResponseDto> findAllMembers(Pageable pageable);

    MemberResponseDto findMemberDto(String loginId);

    Member findMember(String loginId);

    LoginResponseDto findLoginDto(String loginId);

    void deleteById(Long memberId);
}
