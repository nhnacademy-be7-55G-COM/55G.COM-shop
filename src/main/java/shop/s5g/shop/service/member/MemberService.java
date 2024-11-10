package shop.s5g.shop.service.member;


import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member.MemberRegistrationRequestDto;
import shop.s5g.shop.dto.member.MemberResponseDto;
import shop.s5g.shop.entity.member.Member;

public interface MemberService {

    void saveMember(MemberRegistrationRequestDto memberRegistrationRequestDto);

    void updateMember(CustomerUpdateRequestDto updateRequestDto);

    List<MemberResponseDto> findAllMembers(Pageable pageable);

    MemberDetailResponseDto getMemberDto(String loginId);

    Member getMember(String loginId);

    LoginResponseDto getLoginDto(String loginId);

    void deleteById(Long memberId);

    void updateLatestLoginAt(String loginId);
}
