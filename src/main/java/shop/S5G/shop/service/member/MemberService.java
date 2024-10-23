package shop.S5G.shop.service.member;


import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.member.MemberRegistrationDto;
import shop.S5G.shop.dto.member.MemberResponseDto;
import shop.S5G.shop.dto.member.MemberUpdateDto;

import java.util.List;

public interface MemberService {

    void saveMember(MemberRegistrationDto memberRegistrationDto);
    void updateMember(MemberUpdateDto memberUpdateDto);
    MemberResponseDto findMember(String loginId);
    List<MemberResponseDto> findAllMembers(Pageable pageable);
    boolean existsMember(String loginId);
    void deleteById(Long memberId);
}
