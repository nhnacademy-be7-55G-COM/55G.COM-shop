package shop.S5G.shop.service.member;


import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.member.MemberRegistrationRequestDto;
import shop.S5G.shop.dto.member.MemberResponseDto;
import shop.S5G.shop.dto.member.MemberUpdateDto;

public interface MemberService {

    void saveMember(MemberRegistrationRequestDto memberRegistrationRequestDto);

    void updateMember(MemberUpdateDto memberUpdateDto);

    MemberResponseDto findMember(String loginId);

    List<MemberResponseDto> findAllMembers(Pageable pageable);

    boolean existsMember(String loginId);

    void deleteById(Long memberId);
}
